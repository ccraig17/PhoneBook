package com.craig.phonebook;

import android.content.Intent;
import android.net.Uri;
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
    ArrayList<ContactModel> contactList;
    ActivityResultLauncher<Intent> activityResultLauncherForAddImage;
   Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adaptor = new Adaptor(contactList, this);
        btnDisplay = findViewById(R.id.buttonDisplay);
        //register activity for result
        registerActivityForAddImage();
        btnDisplay.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            activityResultLauncherForAddImage.launch(intent);
        });


    }
    public void registerActivityForAddImage() {
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
//                    int resultCode = result.getResultCode();
//                    Intent data = result.getData();
//                    if (resultCode == RESULT_OK && data != null) {
//                        String name = data.getStringExtra("name");
//                        String title = data.getStringExtra("title");
//                        String phone = data.getStringExtra("phone");
//                        String email = data.getStringExtra("email");
//                        String image = data.getStringExtra("image");
//                        Uri selectedImage = Uri.parse(image);
//                        assert selectedImage != null;
//                        contactList.add(new ContactModel(name, title, phone, email, selectedImage));
//                        //contactList.add(new ContactModel(name, title, phone, email, image));
//                        adaptor.notifyDataSetChanged();
//
//                    }

                });
    }

}
