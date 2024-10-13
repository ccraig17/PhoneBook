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
    FloatingActionButton fabAddContact;
    Adaptor adaptor;
    ArrayList<ContactModel> contactList = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncherForAddNewContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        toolbarContactList = findViewById(R.id.toolbarContactList);
        setSupportActionBar(toolbarContactList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fabAddContact = findViewById(R.id.fabAddContact);
        registrationForAddNewContactResult();

        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);

        adaptor = new Adaptor(contactList, ContactListActivity.this);

        //adaptor.setContactModelsList(contactList);
        contactListRecyclerView.setAdapter(adaptor);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddContact.setOnClickListener(view -> {
            Intent intentOpenAddContact = new Intent(this, AddContactActivity.class);
        startActivity(intentOpenAddContact);
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public void registrationForAddNewContactResult() {
        activityResultLauncherForAddNewContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int resultCode = result.getResultCode();
                    if (resultCode == RESULT_OK && intent != null) {
                        String name = intent.getStringExtra("name");
                        String title = intent.getStringExtra("title");
                        String phone = intent.getStringExtra("phone");
                        String email = intent.getStringExtra("email");
                        String selectedImage = intent.getStringExtra("image");
                        if (selectedImage != null) {
                            Uri image = Uri.parse(selectedImage);
                            contactList.add(new ContactModel(name, title, phone, email, image));
                            adaptor.notifyDataSetChanged();
                        }else{
                            contactList.add(new ContactModel(name, title, phone, email, null));
                            adaptor.notifyDataSetChanged();
                            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
                        }
                    }else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "Image selection canceled", Toast.LENGTH_SHORT).show();
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