package com.craig.phonebook.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.craig.phonebook.Adaptor;
import com.craig.phonebook.Model.ContactModel;
import com.craig.phonebook.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnDisplay;
    ArrayList<ContactModel> contactList;
    ActivityResultLauncher<Intent> activityResultLauncherForAddImage;
   Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adaptor = new Adaptor(contactList, this);
        btnDisplay = findViewById(R.id.buttonDisplay);
        btnDisplay.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactListActivity.class);
           startActivity(intent);
        });

    }

}
