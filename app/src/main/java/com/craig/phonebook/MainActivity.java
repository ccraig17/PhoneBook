package com.craig.phonebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button btnDisplay;
    FloatingActionButton fabAddContact;
    ArrayList<ContactModel> contactList;
    ActivityResultLauncher<Intent> activityResultLauncherForAddImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDisplay = findViewById(R.id.buttonDisplay);
        fabAddContact = findViewById(R.id.fabAddContact);

        btnDisplay.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);
        });

        fabAddContact.setOnClickListener(view -> {
             startActivity(new Intent(this, AddContactActivity.class));

        });

    }

}
