package org.example.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class BorrowBook {
    private String userId;
    private String bookId;
    private int requestDate;

    public BorrowBook(String userId, String bookId, int days) {
        this.userId = userId;
        this.bookId = bookId;
        this.requestDate = days;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(int requestDate) {
        this.requestDate = requestDate;
    }
}
