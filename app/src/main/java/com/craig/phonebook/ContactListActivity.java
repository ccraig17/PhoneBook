package com.craig.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ContactListActivity extends AppCompatActivity {
    MaterialToolbar toolbarContactList;
    RecyclerView contactListRecyclerView;
    private FloatingActionButton fabAddContact;
    private Adaptor adaptor;
    private ArrayList<ContactModel> contactList = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncherForAddNewContact;
    private ContactModel contactModel = new ContactModel();
    private DatabaseAccess databaseAccess = new DatabaseAccess(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        toolbarContactList = findViewById(R.id.toolbarContactList);
        setSupportActionBar(toolbarContactList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fabAddContact = findViewById(R.id.fabAddContact);
        registrationForAddContactResult();

        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);

        adaptor = new Adaptor(contactList, ContactListActivity.this);

        //adaptor.setContactModelsList(contactList);
        contactListRecyclerView.setAdapter(adaptor);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            activityResultLauncherForAddNewContact.launch(intent);
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void registrationForAddContactResult() {
        activityResultLauncherForAddNewContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                //check if the user is sending the data.
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if(resultCode == RESULT_OK && data != null){ //means the user had sent data from the AddContactActivity.
                        //transfer incoming data from the AddContactActivity to the ContactListActivity using keyword and object
                        String name = data.getStringExtra("name");
                        String title = data.getStringExtra("title");
                        String phone = data.getStringExtra("phone");
                        String email = data.getStringExtra("email");
                        byte[] image = data.getByteArrayExtra("image");
                        contactList.add(new ContactModel(name, title, phone, email, image));
                        adaptor.notifyDataSetChanged();
                    //SAVE DATA TO DATABASE HERE  databaseAccess.insert(name, title, phone, email, image);
                        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
                    }


                });
    }


//public ArrayList<ContactModel> contactToAdd(ArrayList<ContactModel> contactModels, String name, String title, String phone, String email, String image){
//        ArrayList<ContactModel> contactModelList = new ArrayList<>();
//        contactModelList.add(new ContactModel(name, title, phone, email, image));
//        return contactModelList;
//}

//    private ArrayList<ContactModel> contactList(ArrayList<ContactModel> contactModels){
//            ArrayList<ContactModel> contactModelList = new ArrayList<>();
//            contactModelList.add(new ContactModel("Colin Craig", "Embryologist", "1111111111","colin.ocraig@gmail.com","me"));
//            contactModelList.add(new ContactModel("Curtis Craig", "Software Engineer",  "2222222222","curtis.ocraig@gmail.com","theboyz"));
//            contactModelList.add(new ContactModel("Nola Ogunyemi", "Senior Vice President", "333333333", "nola.ogunyemi@gmail.com", "nola"));
//        return contactModelList;
//    };
}