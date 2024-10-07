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

public class AddContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarAddContact;
    private EditText editTxtName, editeTxtTitle, editTxtPhone, editTxtEmail;
    private Button btnSaveContact;
    CircleImageView imageViewAddImage;
    boolean isImageSelected = false;
    private Uri selectedImage;
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
        editTxtName = findViewById(R.id.editTxtName);
        editeTxtTitle = findViewById(R.id.editTxtTitle);
        editTxtPhone = findViewById(R.id.editTxtPhone);
        editTxtEmail = findViewById(R.id.editTxtEmail);
        btnSaveContact = findViewById(R.id.buttonSaveContact);
        registerForActivity();

        imageViewAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelected();
            }
        });

        btnSaveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTxtName.getText().toString();
                String title = editeTxtTitle.getText().toString();
                String phoneNumber = editTxtPhone.getText().toString();
                String email = editTxtEmail.getText().toString();
                addContact(name, title, phoneNumber, email);
                    Intent intent = new Intent(AddContactActivity.this, ContactListActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("title", title);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("email", email);
                    intent.putExtra("image", selectedImage);
                    startActivity(new Intent(AddContactActivity.this, ContactListActivity.class));
                    finish();
            }
        });
    }
    /*
     * these Methods will be passed to the btnSaveContact when clicked.
     * review how to get Images, converted selected image to Uri image
     * pass Uri selectedImage to Add ContactActivity
     * */
    private boolean addContact(String name, String title, String phoneNumber, String email) {
        if (!name.isEmpty() || !title.isEmpty() || !phoneNumber.isEmpty() || !email.isEmpty()) {
            imageSelected();
            isImageSelected = true;
            return true;
        }else{
            isImageSelected = false;
        }
        return false;
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
            Intent intent = new Intent();
            intent.setType("images/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncherForSelectedImage.launch(intent);
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    };

    public void registerForActivity(){
        activityResultLauncherForSelectedImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if(resultCode == RESULT_OK && data != null){
                    selectedImage = data.getData();
                    imageViewAddImage.setImageURI(selectedImage);
                    isImageSelected = true;
                }else{
                    isImageSelected =false;
                    imageViewAddImage.setImageResource(R.drawable.baseline_person_add_24);
                }
           }
        });
    }


}

