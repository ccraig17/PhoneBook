package com.craig.phonebook.Model;

import java.util.Arrays;
import java.util.Objects;

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
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        ContactModel contactModel = (ContactModel) obj;
        return name.equals(contactModel.name) && title.equals(contactModel.title) && phoneNumber.equals(contactModel.phoneNumber)
                && email.equals(contactModel.email) && Arrays.equals(image, contactModel.image);
    }
    @Override
    public int hashCode() {
//        int result = name.hashCode();
//        result = 31 * result + title.hashCode();
//        result = 31 * result + phoneNumber.hashCode();
//        result = 31 * result + email.hashCode();
//        result = 31 * result + Arrays.hashCode(image);
//        return result;
        return Objects.hash(name, title, phoneNumber, email, Arrays.hashCode(image));
    }
}
