package com.craig.phonebook.Model;

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

    public String getTitle() {
        return title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getImage() {
        return image;
    }
}
