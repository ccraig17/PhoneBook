package com.craig.phonebook;

import android.net.Uri;

public class ContactModel {
    private String name;
    private String title;
    private String phoneNumber;
    private String email;
    private byte[] image;

    public ContactModel() {
    }
    public ContactModel(String name, String title, String phoneNumber, String email) {
        this.name = name;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public ContactModel(String name, String title, String phoneNumber, String email, byte[] image) {
        this.name = name;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }




}
