package org.example;

import org.example.service.BookBorrowService;
import org.example.service.BookService;
import org.example.service.UserService;
import org.example.service.WaitListService;

import java.util.*;

public class LibraryManagementSystem {
    BookService bookService = new BookService();
    UserService userService = new UserService();
    WaitListService waitListService = new WaitListService();
    BookBorrowService bookBorrowService = new BookBorrowService(userService, bookService, waitListService);

    public LibraryManagementSystem() {

    }

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