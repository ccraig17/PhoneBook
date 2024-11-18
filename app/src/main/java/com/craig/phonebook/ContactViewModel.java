package com.craig.phonebook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.craig.phonebook.Model.ContactModel;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private MutableLiveData<List<ContactModel>> mContacts = new MutableLiveData<>();
    public LiveData<List<ContactModel>> getContacts() {
        return mContacts;
    }
}
