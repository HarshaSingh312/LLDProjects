package org.example.service;

import org.example.model.Book;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class BookService {
    private HashMap<String, Book> booksDB = new HashMap<>();
    // title:author -> bookId
    private HashMap<String, String> booksIdDB = new HashMap<>();
    private HashMap<String, Integer> bookingIdGenerationPrefixCount = new HashMap<>();

    public BookService() {

    }

    private String generateBookId(String author) {
        String[] names = author.split(" ");
        String lastName = names[names.length - 1];
        String prefix;
        if (lastName.length() < 3){
            prefix = lastName;
        } else {
            prefix = lastName.substring(0, 3);
        }
        int count = bookingIdGenerationPrefixCount.getOrDefault(prefix, 999);
        count++;
        prefix = prefix.toUpperCase(Locale.ROOT);
        bookingIdGenerationPrefixCount.put(prefix, count);
        return prefix + count;
    }

    public boolean isValidBook(String bookId) {
//        System.out.println(bookId);
//        System.out.println(this.booksDB);
        return booksDB.containsKey(bookId);
    }

    public int getBookCount(String bookId) {
        return booksDB.get(bookId).getCopies();
    }


    public void reduceBookCount(String bookId, int count) {
        booksDB.get(bookId).addCopies(-count);
    }

    public void addBookCount(String bookId, int count) {
        booksDB.get(bookId).addCopies(count);
    }

    public String addBook(String title, String author, int copies) {
//        System.out.println("sdgdfgdfh");
        if (copies <= 0) return "INVALID_COPIES";
        if (title.isBlank() || author.isBlank()) return "INVALID_INPUT";
        // Check if we already have that book
        String bookingId = booksIdDB.getOrDefault(title + ":" + author, null);
        if (Objects.isNull(bookingId)) {
            bookingId = generateBookId(author);
            booksIdDB.put(title + ":" + author, bookingId);
            booksDB.put(bookingId, new Book(author, title, copies));
        } else {
            Book book = booksDB.get(bookingId);
            book.addCopies(copies);
            booksDB.put(bookingId, book);
        }
//        System.out.println(booksDB.get(bookingId));
        return bookingId;
    }
}
