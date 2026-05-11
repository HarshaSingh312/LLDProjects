//package library;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryManagementSystem {

    // ── Models ──
    static class Book {
        private String authorId;
        private String title;
        private int copies;

        Book(String authorId, String title, int copies) {
            this.authorId = authorId;
            this.title = title;
            this.copies = copies;
        }

        String getAuthorId() { return authorId; }
        void setAuthorId(String authorId) { this.authorId = authorId; }
        String getTitle() { return title; }
        void setTitle(String title) { this.title = title; }
        int getCopies() { return copies; }
        void addCopies(int copies) { this.copies += copies; }
    }

    static class BorrowBook {
        private String userId;
        private String bookId;
        private int requestDate;

        BorrowBook(String userId, String bookId, int days) {
            this.userId = userId;
            this.bookId = bookId;
            this.requestDate = days;
        }

        String getBookId() { return bookId; }
        void setBookId(String bookId) { this.bookId = bookId; }
        int getRequestDate() { return requestDate; }
        void setRequestDate(int requestDate) { this.requestDate = requestDate; }
    }

    static class User {
        private String userId;
        private String name;

        User(String userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        String getUserId() { return userId; }
        void setUserId(String userId) { this.userId = userId; }
        String getName() { return name; }
        void setName(String name) { this.name = name; }
    }

    // ── Services ──

    static class BookService {
        private HashMap<String, Book> booksDB = new HashMap<>();
        private HashMap<String, String> booksIdDB = new HashMap<>();
        private HashMap<String, Integer> bookingIdGenerationPrefixCount = new HashMap<>();

        private String generateBookId(String author) {
            String[] names = author.split(" ");
            String lastName = names[names.length - 1];
            String prefix = lastName.length() < 3 ? lastName : lastName.substring(0, 3);
            prefix = prefix.toUpperCase(Locale.ROOT);
            int count = bookingIdGenerationPrefixCount.getOrDefault(prefix, 999) + 1;
            bookingIdGenerationPrefixCount.put(prefix, count);
            return prefix + count;
        }

        boolean isValidBook(String bookId) { return booksDB.containsKey(bookId); }
        int getBookCount(String bookId) { return booksDB.get(bookId).getCopies(); }
        void reduceBookCount(String bookId, int count) { booksDB.get(bookId).addCopies(-count); }
        void addBookCount(String bookId, int count) { booksDB.get(bookId).addCopies(count); }

        String addBook(String title, String author, int copies) {
            if (copies <= 0) return "INVALID_COPIES";
            if (title.isBlank() || author.isBlank()) return "INVALID_INPUT";
            String key = title + ":" + author;
            String bookingId = booksIdDB.getOrDefault(key, null);
            if (Objects.isNull(bookingId)) {
                bookingId = generateBookId(author);
                booksIdDB.put(key, bookingId);
                booksDB.put(bookingId, new Book(author, title, copies));
            } else {
                booksDB.get(bookingId).addCopies(copies);
            }
            return "BOOK_ID," + bookingId;
        }
    }

    static class UserService {
        Map<String, User> usersDB = new HashMap<>();

        boolean isValidUser(String userId) { return usersDB.containsKey(userId); }

        String addUser(String userId, String name) {
            if (userId.isBlank() || name.isBlank()) return "INVALID_INPUT";
            if (usersDB.containsKey(userId)) return "USER_ALREADY_EXISTS";
            usersDB.put(userId, new User(userId, name));
            return "SUCCESS";
        }

        String removeUser(String userId) {
            if (userId.isBlank()) return "INVALID_INPUT";
            if (!usersDB.containsKey(userId)) return "USER_NOT_FOUND";
            usersDB.remove(userId);
            return "SUCCESS";
        }
    }

    static class WaitListService {
        HashMap<String, List<String>> waitingList = new HashMap<>();

        boolean isUserInWaitList(String userName) {
            for (List<String> users : waitingList.values())
                for (String user : users)
                    if (user.equals(userName)) return true;
            return false;
        }

        String getFirstUser(String bookId) {
            if (!waitingList.containsKey(bookId) || waitingList.get(bookId).isEmpty()) return null;
            return waitingList.get(bookId).get(0);
        }

        void removeFirstUser(String bookId) {
            if (!waitingList.containsKey(bookId) || waitingList.get(bookId).isEmpty()) return;
            waitingList.get(bookId).remove(0);
        }

        boolean isUserInWaitList(String userName, String bookId) {
            return waitingList.getOrDefault(bookId, new ArrayList<>()).contains(userName);
        }

        String addToWaitList(String userId, String bookId) {
            if (isUserInWaitList(userId, bookId)) return "ALREADY_WAITLISTED";
            List<String> users = waitingList.getOrDefault(bookId, new ArrayList<>());
            users.add(userId);
            waitingList.put(bookId, users);
            return "WAITLISTED," + users.size();
        }
    }

    static class BookBorrowService {
        HashMap<String, List<BorrowBook>> borrowBookDB = new HashMap<>();
        HashMap<String, List<String>> heldFor = new HashMap<>();
        BookService bookService;
        WaitListService waitListService;
        UserService userService;

        BookBorrowService(UserService userService, BookService bookService, WaitListService waitListService) {
            this.userService = userService;
            this.bookService = bookService;
            this.waitListService = waitListService;
        }

        boolean doesUserHasBooks(String userName) { return borrowBookDB.containsKey(userName); }

        boolean bookAlreadyIssue(String bookId, String userID) {
            for (BorrowBook b : borrowBookDB.getOrDefault(userID, new ArrayList<>()))
                if (b.getBookId().equals(bookId)) return true;
            return false;
        }

        String requestBorrow(String userId, String bookId, int requestDay) {
            if (!userService.isValidUser(userId)) return "USER_NOT_FOUND";
            if (!bookService.isValidBook(bookId)) return "BOOK_NOT_FOUND";
            if (requestDay < 0) return "INVALID_DAY";
            if (bookAlreadyIssue(bookId, userId)) return "ALREADY_ISSUED_TO_USER";
            if (bookService.getBookCount(bookId) == 0) {
                if (waitListService.isUserInWaitList(userId, bookId)) return "ALREADY_WAITLISTED";
                List<String> heldUser = heldFor.getOrDefault(bookId, new ArrayList<>());
                if (!Objects.isNull(heldUser) && heldUser.contains(userId)) {
                    List<BorrowBook> books = borrowBookDB.getOrDefault(userId, new ArrayList<>());
                    books.add(new BorrowBook(userId, bookId, requestDay));
                    borrowBookDB.put(userId, books);
                    heldUser.remove(userId);
                    heldFor.put(bookId, heldUser);
                    return "ISSUED";
                }
                return waitListService.addToWaitList(userId, bookId);
            } else {
                bookService.reduceBookCount(bookId, 1);
                List<BorrowBook> books = borrowBookDB.getOrDefault(userId, new ArrayList<>());
                books.add(new BorrowBook(userId, bookId, requestDay));
                borrowBookDB.put(userId, books);
                return "ISSUED";
            }
        }

        String returnBook(String userId, String bookId, int returnDay) {
            if (!userService.isValidUser(userId)) return "USER_NOT_FOUND";
            if (!bookService.isValidBook(bookId)) return "BOOK_NOT_FOUND";
            if (!bookAlreadyIssue(bookId, userId)) return "NOT_ISSUED_TO_USER";
            if (returnDay < 0) return "INVALID_DAY";
            List<BorrowBook> borrowBooks = borrowBookDB.get(userId);
            for (BorrowBook borrowBook : borrowBooks) {
                if (borrowBook.getBookId().equals(bookId)) {
                    if (returnDay < borrowBook.getRequestDate()) return "INVALID_DAY";
                    bookService.addBookCount(bookId, 1);
                    if (!Objects.isNull(waitListService.getFirstUser(bookId))) {
                        List<String> heldUsers = heldFor.getOrDefault(bookId, new ArrayList<>());
                        heldUsers.add(waitListService.getFirstUser(bookId));
                        heldFor.put(bookId, heldUsers);
                        waitListService.removeFirstUser(bookId);
                        bookService.reduceBookCount(bookId, 1);
                    }
                    int days = returnDay - borrowBook.getRequestDate();
                    int fine = days > 14 ? (days - 14) * 20 : 0;
                    borrowBooks.remove(borrowBook);
                    if (borrowBooks.isEmpty()) {
                        borrowBookDB.remove(userId);
                    } else {
                        borrowBookDB.put(userId, borrowBooks);
                    }
                    return "RETURNED," + fine;
                }
            }
            return "NOT_ISSUED";
        }

        List<String> usersHavingBook(String bookId) {
            if (!bookService.isValidBook(bookId)) return new ArrayList<>();
            return borrowBookDB.entrySet().stream()
                    .filter(s -> s.getValue().stream().anyMatch(b -> b.getBookId().equals(bookId)))
                    .map(HashMap.Entry::getKey).sorted().collect(Collectors.toList());
        }

        List<String> booksIssuedToUser(String userId) {
            if (!userService.isValidUser(userId)) return new ArrayList<>();
            List<String> books = new ArrayList<>();
            borrowBookDB.getOrDefault(userId, new ArrayList<>()).forEach(b -> books.add(b.getBookId()));
            Collections.sort(books);
            return books;
        }
    }

    // ── LibraryManagementSystem (public facade) ──

    BookService bookService = new BookService();
    UserService userService = new UserService();
    WaitListService waitListService = new WaitListService();
    BookBorrowService bookBorrowService = new BookBorrowService(userService, bookService, waitListService);

    public String addBook(String title, String author, int copies) {
        return bookService.addBook(title, author, copies);
    }

    public String registerUser(String userId, String name) {
        return userService.addUser(userId, name);
    }

    public String unregisterUser(String userId) {
        if (bookBorrowService.doesUserHasBooks(userId)) return "USER_HAS_ISSUED_BOOKS";
        if (waitListService.isUserInWaitList(userId)) return "USER_IN_WAITLIST";
        return userService.removeUser(userId);
    }

    public String requestBorrow(String userId, String bookId, int requestDay) {
        return bookBorrowService.requestBorrow(userId, bookId, requestDay);
    }

    public String returnBook(String userId, String bookId, int returnDay) {
        return bookBorrowService.returnBook(userId, bookId, returnDay);
    }

    public List<String> usersHavingBook(String bookId) {
        return bookBorrowService.usersHavingBook(bookId);
    }

    public List<String> booksIssuedToUser(String userId) {
        return bookBorrowService.booksIssuedToUser(userId);
    }

}
