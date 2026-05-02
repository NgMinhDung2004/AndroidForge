package com.example.lab5_bai2_listview;

public class Person {
    private String name;
    private int imageResId;

    public Person(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}