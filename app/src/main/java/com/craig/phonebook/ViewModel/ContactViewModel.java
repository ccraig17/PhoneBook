package com.craig.phonebook.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.craig.phonebook.Repository.MyContactRepository;
import com.craig.phonebook.RoomDatabase.Contact;
import com.craig.phonebook.RoomDatabase.ContactDatabase;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private MyContactRepository repository;
    private LiveData<List<Contact>> contactList;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new MyContactRepository(application);
        contactList = repository.getAllContacts();
    }
    /*
    * define ALL database operations within the ViewModel Class via the repository object
    * */
    public void insert(Contact contact){
        repository.insert(contact);
    }
    public void delete(Contact contact){
        repository.delete(contact);
    }
    public void update(Contact contact){
        repository.update(contact);
    }
    public LiveData<List<Contact>> getAllContacts(){
        return contactList;
    }
}
