package com.craig.phonebook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarAddContact;
    private EditText editTxtUpdateName, editeTxtUpdateTitle, editTxtUpdatePhone, editTxtUpdateEmail;
    private Button btnUpdateContact;
    CircleImageView imageViewUpdateImage;
    boolean isImageSelected = false;
    private ActivityResultLauncher<Intent> activityResultLauncherForSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        toolbarAddContact = findViewById(R.id.toolbarAddContact);
        toolbarAddContact.setNavigationOnClickListener(view -> {
            if (Objects.equals(toolbarAddContact.getTitle(), "Add Contact")) {
                startActivity(new Intent(this, ContactListActivity.class));
                finish();
            }
        });
        editTxtUpdateName = findViewById(R.id.editTxtName);
        editeTxtUpdateTitle = findViewById(R.id.editTxtTitle);
        editTxtUpdatePhone = findViewById(R.id.editTxtPhone);
        editTxtUpdateEmail = findViewById(R.id.editTxtEmail);
        btnUpdateContact = findViewById(R.id.buttonSaveContact);
        registerForActivity();

        imageViewUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelected();
            }
        });

        btnUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateName = editTxtUpdateName.getText().toString();
                String updateTitle = editeTxtUpdateTitle.getText().toString();
                String updatePhoneNumber = editTxtUpdatePhone.getText().toString();
                String updateEmail = editTxtUpdateEmail.getText().toString();
                addContact(updateName, updateTitle, updatePhoneNumber, updateEmail);
                if (isImageSelected) {
                    Intent intent = new Intent(UpdateContactActivity.this, ContactListActivity.class);
                    intent.putExtra("name", updateName);
                    intent.putExtra("title", updateTitle );
                    intent.putExtra("phoneNumber", updatePhoneNumber);
                    intent.putExtra("email", updateEmail);
                    intent.putExtra("image", isImageSelected);
                    startActivity(new Intent(UpdateContactActivity.this, ContactListActivity.class));
                    finish();
                }
            }
        });
    }
    /*
     * these Methods will be passed to the btnSaveContact when clicked.
     * review how to get Images, converted selected image to Uri image
     * pass Uri selectedImage to Add ContactActivity
     * */
    private boolean addContact(String name, String title, String phoneNumber, String email) {
        if (name.isEmpty() || title.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{

        }

        return true;
    }

    private void imageSelected() {
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncherForSelectedImage.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imageSelected();
        }
    };

    public void registerForActivity(){
        activityResultLauncherForSelectedImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult resultActivity) {
                int resultCode = resultActivity.getResultCode();
                Intent data = resultActivity.getData();

                if(resultCode == RESULT_OK && data != null){
                    Uri selectedImage = data.getData();
                    imageViewUpdateImage.setImageURI(selectedImage);
                    isImageSelected = true;
                }else{
                    isImageSelected =false;
                    imageViewUpdateImage.setImageResource(R.drawable.baseline_person_add_24);
                }
           }
        });
    }

}

