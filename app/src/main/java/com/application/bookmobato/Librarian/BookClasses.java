package com.application.bookmobato.Librarian;

public class BookClasses {

    String title, author, genre, publishdate, numpages, description;

    public BookClasses() {

    }

    public BookClasses(String title, String author, String genre, String publishdate, String numpages, String description) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishdate = publishdate;
        this.numpages = numpages;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublishdate() {
        return publishdate;
    }

    public String getNumpages() {
        return numpages;
    }

    public String getDescription() {
        return description;
    }
}