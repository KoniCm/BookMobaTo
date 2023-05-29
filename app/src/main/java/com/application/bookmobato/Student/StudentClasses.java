package com.application.bookmobato.Student;

public class StudentClasses {
    String id, name, section, strand, gradelevel, pass, image, key;

    public StudentClasses() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public StudentClasses(String id, String name, String section, String strand, String gradelevel, String pass, String image) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.strand = strand;
        this.gradelevel = gradelevel;
        this.pass = pass;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getStrand() {
        return strand;
    }

    public String getGradelevel() {
        return gradelevel;
    }

    public String getPass() {
        return pass;
    }

    public String getImage() {
        return image;
    }
}