package org.example.model;

public class Book {
    private String authorId;
    private String title;

    public int getCopies() {
        return copies;
    }

    private int copies;

    public Book(String authorId, String title, int copies) {
        this.authorId = authorId;
        this.title = title;
        this.copies = copies;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addCopies(int copies) {
        this.copies += copies;
    }
}
