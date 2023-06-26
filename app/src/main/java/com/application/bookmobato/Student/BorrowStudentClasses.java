package com.application.bookmobato.Student;

public class BorrowStudentClasses {
    String title, name , setBorrowedDate, image, key;

    public BorrowStudentClasses() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public BorrowStudentClasses(String title, String name, String image, String setBorrowedDate) {
        this.title = title;
        this.name = name;
        this.image = image;
        this.setBorrowedDate = setBorrowedDate;
    }

    public String getTitle() {
        return this.title;
    }
    public String getName() {
        return this.name;
    }
    public String getImage() {
        return this.image;
    }
    public String getSetBorrowedDate() {
        return setBorrowedDate;
    }
}