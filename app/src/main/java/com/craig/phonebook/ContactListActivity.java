package com.craig.phonebook;

import android.content.Intent;
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
    ArrayList<ContactModel> contactModelList;
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

        adaptor = new Adaptor(contactList(contactModelList), this);
        contactListRecyclerView.setAdapter(adaptor);


    }
    public void registerActivityForAddImage(){
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // this will take the image the user selected in the AddImageActivity along with Name, Title, PhoneNumber, Email and Image
                        //ALL will then be added to the database.
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            String name = data.getStringExtra("name");
                            String title = data.getStringExtra("title");
                            String phoneNumber = data.getStringExtra("phoneNumber");
                            String email = data.getStringExtra("email");
                            String image = data.getStringExtra("image");
                            contactModelList.add(new ContactModel(name, title, phoneNumber, email, image));
                            adaptor.notifyDataSetChanged();
                        }
                    }
                });
    }

    private ArrayList<ContactModel> contactList(ArrayList<ContactModel> contactModels){
            ArrayList<ContactModel> contactModelList = new ArrayList<>();
            contactModelList.add(new ContactModel("Colin Craig", "Embryologist", "1111111111","colin.ocraig@gmail.com","me"));
            contactModelList.add(new ContactModel("Curtis Craig", "Software Engineer",  "2222222222","curtis.ocraig@gmail.com","theboyz"));
            contactModelList.add(new ContactModel("Nola Ogunyemi", "Senior Vice President", "333333333", "nola.ogunyemi@gmail.com", "nola"));
        return contactModelList;
    };
}