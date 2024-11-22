package com.craig.phonebook;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.craig.phonebook.Model.ContactModel;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private final MutableLiveData<List<ContactModel>> contactsList = new MutableLiveData<>();

    public ContactViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ContactModel>> getContacts() {
        return contactsList;
    }
}
