package com.craig.phonebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    Adaptor adaptor;
    ArrayList<ContactModel> contactList = new ArrayList<>();
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
        adaptor = new Adaptor(contactList, ContactListActivity.this);
        //adaptor.setContactModelsList(contactList);
        contactListRecyclerView.setAdapter(adaptor);
        

    }


    public void registerActivityForAddImage() {
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if (resultCode == RESULT_OK && data != null) {
                        String name = data.getStringExtra("name");
                        String title = data.getStringExtra("title");
                        String phone = data.getStringExtra("phone");
                        String email = data.getStringExtra("email");
                        String image = data.getStringExtra("image");
                        contactList.add(new ContactModel(name, title, phone, email, image));
                        // adaptor.setContactModelsList(contactList);

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