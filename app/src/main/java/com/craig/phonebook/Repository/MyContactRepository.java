package com.craig.phonebook.Repository;
import android.app.Application;
import androidx.lifecycle.LiveData;
import com.craig.phonebook.RoomDatabase.Contact;
import com.craig.phonebook.RoomDatabase.ContactDAO;
import com.craig.phonebook.RoomDatabase.ContactDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
* The purpose of the Repository is to:
* - store and manage the data shown on the UI bypassing the LifeCycle of the App
* - and maintaining/updating any changes to the data
* An instance of the DAO class is created in the constructor of the Repository class.
* A reference to the ContactList under LiveData
* -In the Constructor an instance of the Database (contactDatabase) is created;
* -The DAO (contactDAO = contactDatabase.contactDAO();) is defined using the database instance
* - the contactDAO = contactDatabase.contactDAO() the passes ALL the elements of the Contact into the contactList from the DB
* ExecutorService executorService = Executors.newSingleThreadExecutor(); creates a different in the background to run the operations (CRUD)
* */
public class MyContactRepository {
    private ContactDAO contactDAO;
    private LiveData<List<Contact>> contactList;
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public MyContactRepository(Application application) { // in this construct: create obj from db class. the constructor argument is one of type Application
        ContactDatabase contactDatabase = ContactDatabase.getInstance(application);
        contactDAO = contactDatabase.contactDAO();
        contactList = contactDAO.getAllContacts();
    }

    public void insert(Contact contact){
        executorService.execute(new Runnable() { //creates a new Runnable as an agreement to run the operation in the background
            @Override
            public void run() {
                contactDAO.insert(contact);
            }
        });
    }
    
    public void delete(Contact contact){
        executorService.execute(() -> contactDAO.delete(contact));
    }

    public void update(Contact contact){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                contactDAO.update(contact);
            }
        });
    }

    public LiveData<List<Contact>> getAllContacts(){
        return contactList;
    }
}
