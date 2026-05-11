package org.example.service;

import org.example.model.BorrowBook;

import java.util.*;

public class BookBorrowService {

    ///  User to Book mapping
    HashMap<String, List<BorrowBook>> borrowBookDB = new HashMap<>();
    // book to user
    HashMap<String, String> heldFor = new HashMap<>();
    BookService bookService;
    WaitListService waitListService;
    UserService userService;

    public BookBorrowService(UserService userService, BookService bookService, WaitListService waitListService) {
        this.userService = userService;
        this.bookService = bookService;
        this.waitListService = waitListService;
    }

    public boolean doesUserHasBooks(String userName) {
        return borrowBookDB.containsKey(userName);
    }

    public int getCount(String bookId) {
        return bookService.getBookCount(bookId);
    }

    public boolean bookAlreadyIssue(String bookId, String userID) {
        List<BorrowBook> books = borrowBookDB.getOrDefault(userID, new ArrayList<>());
        for (BorrowBook borrowBook: books) {
            if (borrowBook.getBookId().equals(bookId)) return true;
        }
        return false;
    }

    public String requestBorrow(String userId, String bookId, int requestDay) {
        if (!userService.isValidUser(userId)) return "USER_NOT_FOUND";
        if (!bookService.isValidBook(bookId)) return "BOOK_NOT_FOUND";
        if (requestDay < 0) return "INVALID_DAY";
        // Book already issued?
        if (bookAlreadyIssue(bookId, userId)) return "ALREADY_ISSUED_TO_USER";
        if (getCount(bookId) == 0) {
            if (waitListService.isUserInWaitList(userId, bookId)) {
                return "ALREADY_WAITLISTED";
            }
            String heldUser = heldFor.getOrDefault(bookId, null);
            if (!Objects.isNull(heldUser) && heldUser.equals(userId)) {
                List<BorrowBook> borrowBooks = borrowBookDB.getOrDefault(userId, new ArrayList<>());
                borrowBooks.add(new BorrowBook(userId, bookId, requestDay));
                borrowBookDB.put(userId, borrowBooks);
                return "ISSUED";
            }
            // Add to waitList
            return waitListService.addToWaitList(userId, bookId);
        } else {
            // issue book
            // reduce book count
            bookService.reduceBookCount(bookId, 1);
            // Issue book
            List<BorrowBook> borrowBooks = borrowBookDB.getOrDefault(userId, new ArrayList<>());
            borrowBooks.add(new BorrowBook(userId, bookId, requestDay));
            borrowBookDB.put(userId, borrowBooks);
            return "ISSUED";
        }
    }

    public String returnBook(String userId, String bookId, int returnDay) {
        if (!userService.isValidUser(userId)) return "USER_NOT_FOUND";
        if (!bookService.isValidBook(bookId)) return "BOOK_NOT_FOUND";
        if (!bookAlreadyIssue(bookId, userId)) return "NOT_ISSUED_TO_USER";
        if (returnDay < 0) return "INVALID_DAY";
        List<BorrowBook> borrowBooks = borrowBookDB.get(userId);
        for (BorrowBook borrowBook: borrowBooks) {
            if (borrowBook.getBookId().equals(bookId)) {
                if (returnDay < borrowBook.getRequestDate()) return "INVALID_DAY";
//                borrowBooks.remove(borrowBook);
                // check if any user is in waiting list
                // if yes then issue book to first user
                bookService.addBookCount(bookId, 1);
                if (!Objects.isNull(waitListService.getFirstUser(bookId))) {
                    heldFor.put(bookId, waitListService.getFirstUser(bookId));
                    waitListService.removeFirstUser(bookId);
                    bookService.reduceBookCount(bookId, 1);
                }

                int days = returnDay - borrowBook.getRequestDate();
                int fine = days > 14? (days - 14) * 20: 0;
                borrowBookDB.remove(userId);
                return "RETURNED," + fine;
            }
        }
        return "NOT_ISSUED";
    }

    public List<String> usersHavingBook(String bookId) {
        if (!bookService.isValidBook(bookId)) return new ArrayList<>();
        return borrowBookDB.entrySet().stream().filter(s ->
                        s.getValue().stream().anyMatch(b -> b.getBookId().equals(bookId)))
                .map(HashMap.Entry::getKey).sorted().collect(java.util.stream.Collectors.toList());
    }

    public List<String> booksIssuedToUser(String userId) {
        if (!userService.isValidUser(userId)) return new ArrayList<>();
        List<String> books = new ArrayList<>();
        borrowBookDB.getOrDefault(userId, new ArrayList<>()).forEach(borrowBook -> books.add(borrowBook.getBookId()));
        Collections.sort(books);
        return books;
    }
}
