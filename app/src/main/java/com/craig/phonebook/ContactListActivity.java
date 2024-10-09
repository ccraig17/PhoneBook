package com.craig.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.Objects;

public class ContactListActivity extends AppCompatActivity {
    MaterialToolbar toolbarContactList;
    RecyclerView contactListRecyclerView;
    ArrayList<ContactModel> contactList;
    Adaptor adaptor;
    ActivityResultLauncher<Intent> activityResultLauncherForAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        toolbarContactList = findViewById(R.id.toolbarContactList);
        setSupportActionBar(toolbarContactList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //register activity for result
        registerActivityForAddImage();
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new Adaptor(contactList, this);
        adaptor.setContactModelsList(contactList);
        contactListRecyclerView.setAdapter(adaptor);

    }


    public void registerActivityForAddImage(){
        Intent intent = new Intent();
        intent.getStringExtra("name");
        intent.getStringExtra("title");
        intent.getStringExtra("phone");
        intent.getStringExtra("email");
        String name = contactList.get(0).getName();
        String title = contactList.get(0).getTitle();
        String phone = contactList.get(0).getPhoneNumber();
        String email = contactList.get(0).getEmail();
        Uri image = contactList.get(0).getImage();
        contactList.add(new ContactModel(name, title, phone, email, image));
        activityResultLauncherForAddImage.launch(intent);

    }
public ArrayList<ContactModel> contactList(ArrayList<ContactModel> contactModels, String name, String title, String phone, String email, Uri image){
        ArrayList<ContactModel> contactModelList = new ArrayList<>();
        contactModelList.add(new ContactModel(name, title, phone, email, image));
        return contactModelList;
}

//    private ArrayList<ContactModel> contactList(ArrayList<ContactModel> contactModels){
//            ArrayList<ContactModel> contactModelList = new ArrayList<>();
//            contactModelList.add(new ContactModel("Colin Craig", "Embryologist", "1111111111","colin.ocraig@gmail.com","me"));
//            contactModelList.add(new ContactModel("Curtis Craig", "Software Engineer",  "2222222222","curtis.ocraig@gmail.com","theboyz"));
//            contactModelList.add(new ContactModel("Nola Ogunyemi", "Senior Vice President", "333333333", "nola.ogunyemi@gmail.com", "nola"));
//        return contactModelList;
//    };
}