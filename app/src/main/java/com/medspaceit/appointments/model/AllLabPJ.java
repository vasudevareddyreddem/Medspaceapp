package com.medspaceit.appointments.model;

/**
 * Created by Bhupi on 14-Nov-18.
 */

public class AllLabPJ {
    String name;
    String image;
    String test;
    String practice;
    public AllLabPJ(String name, String image, String test, String practice) {
        this.name=name;
        this.image=image;
        this.test=test;
        this.practice=practice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }
}
