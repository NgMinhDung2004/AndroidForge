package com.example.lab5_bai1;

public class Person {
    private int imagePerson;
    private String namePerson;

    public Person(int imagePerson, String namePerson) {
        this.imagePerson = imagePerson;
        this.namePerson = namePerson;
    }

    public int getImagePerson() {
        return imagePerson;
    }

    public void setImagePerson(int imagePerson) {
        this.imagePerson = imagePerson;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }
}