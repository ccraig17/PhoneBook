package com.craig.phonebook.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craig.phonebook.Adaptor;
import com.craig.phonebook.RoomDatabase.Contact;
import com.craig.phonebook.R;
import com.craig.phonebook.ViewModel.ContactViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import java.util.Objects;

public class ContactListActivity extends AppCompatActivity {
    MaterialToolbar toolbarContactList;
    RecyclerView contactListRecyclerView;
    private FloatingActionButton fabAddContact;
    private Adaptor adapter;
    private ActivityResultLauncher<Intent> activityResultLauncherForAddNewContact;
    private ActivityResultLauncher<Intent> activityResultLauncherForUpdateContact;
    private ConstraintLayout main;
    private ContactViewModel contactViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        toolbarContactList = findViewById(R.id.toolbarContactList);
        setSupportActionBar(toolbarContactList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        fabAddContact = findViewById(R.id.fabAddContact);
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        main = findViewById(R.id.main);

        registrationForAddContactResult();
        registrationForUpdateContactResult();

        adapter = new Adaptor();
        contactListRecyclerView.setAdapter(adapter);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContactList(contacts);
            }
        });


        fabAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
            activityResultLauncherForAddNewContact.launch(intent);
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition(); //gets the position of the item/contact object that is being swiped
                contactViewModel.delete(adapter.getPosition(position));
            }
        }).attachToRecyclerView(contactListRecyclerView);

        //used to click on and select the contact to be update and send the current data to Update Contact Activity
        //since data is being sent to another activity, a resultLauncher() is needed, and resignation of the Launcher is called in the OnCreate() method
        // this Registration Method will be used to rec'd the update contact data from the UpdateContactActivity and the update the contact in the database,
        // and the adaptor and therefore the RecyclerView
        adapter.setListener(new Adaptor.OnImageClickListener() {
            @Override
            public void onImageClick(Contact contact) {
                //use this to send the contact object's data to the UpdateContactActivity via intent
                Intent intent = new Intent(ContactListActivity.this, UpdateContactActivity.class);
                intent.putExtra("id", contact.getId()); //use the contact.getId() to update the contact in the database
                intent.putExtra("name", contact.getName());
                intent.putExtra("title", contact.getTitle());
                intent.putExtra("phone", contact.getPhoneNumber());
                intent.putExtra("email", contact.getEmail());
                intent.putExtra("image", contact.getImage());
                //start activity with activity ResultLauncher
               activityResultLauncherForUpdateContact.launch(intent);

            }
        });

    }
    public void registrationForUpdateContactResult() {
        activityResultLauncherForUpdateContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                Intent data = result.getData();
                if (resultCode == RESULT_OK && data != null) {
                    int id = data.getIntExtra("id", -1);
                    String updateName = data.getStringExtra("updateName");
                    String updateTitle = data.getStringExtra("updateTitle");
                    String updatePhone = data.getStringExtra("updatePhone");
                    String updateEmail = data.getStringExtra("updateEmail");
                    byte[] image = data.getByteArrayExtra("image");
                    Contact contact = new Contact(updateName, updateTitle, updatePhone, updateEmail, image);
                    contact.setId(id);
                    contactViewModel.update(contact);
                    Toast.makeText(ContactListActivity.this, "Contact Updated", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    public void registrationForAddContactResult() {
        activityResultLauncherForAddNewContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                //check if the user is sending the data.
                    int resultCode = result.getResultCode();
                    Intent data = result.getData(); //the data that is the result of intent
                    if(resultCode == RESULT_OK && data != null){ //means the user had sent data from the AddContactActivity.
                        //transfer incoming data from the AddContactActivity to the ContactListActivity using keyword and object
                        String name = data.getStringExtra("name");
                        String title = data.getStringExtra("title");
                        String phone = data.getStringExtra("phone");
                        String email = data.getStringExtra("email");
                        byte[] image = data.getByteArrayExtra("image");
                        Contact contact = new Contact(name, title, phone, email, image);
                        contactViewModel.insert(contact);
                        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}