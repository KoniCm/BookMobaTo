package com.application.bookmobato.Librarian;

public class BookClasses {

    String title, author, genre, publishdate, numpages, description, image, key;

    public BookClasses() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public BookClasses(String title, String author, String genre, String publishdate, String numpages, String description, String image) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishdate = publishdate;
        this.numpages = numpages;
        this.description = description;
        this.image = image;
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