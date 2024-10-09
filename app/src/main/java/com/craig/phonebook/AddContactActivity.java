package com.craig.phonebook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
        imageViewAddImage = findViewById(R.id.imageViewAddImage);
        btnSaveContact = findViewById(R.id.buttonSaveContact);
        registerForActivityForSelectedImage();

        imageViewAddImage.setOnClickListener(view -> {
            imageChooser();
        });
        btnSaveContact.setOnClickListener(view -> {
            String name = editTxtName.getText().toString();
            String title = editeTxtTitle.getText().toString();
            String phone = editTxtPhone.getText().toString();
            String email = editTxtEmail.getText().toString();
            if(isImageSelected){
                addContact(name, title, phone, email, selectedImage);
            }else{
                addContact(name, title, phone, email, null);
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent();
            intent.putExtra("name", name);
            intent.putExtra("title", title);
            intent.putExtra("phone", phone);
            intent.putExtra("email", email);
            intent.putExtra("image", selectedImage);
            setResult(RESULT_OK, intent);
            startActivity(new Intent(this, ContactListActivity.class));
            finish();
        });

    }
    /*
     * these Methods will be passed to the btnSaveContact when clicked.
     * review how to get Images, converted selected image to Uri image
     * pass Uri selectedImage to Add ContactActivity
     * */
    public ContactModel addContact(String name, String title, String phone, String email, Uri image){
        if(!name.isEmpty() && !title.isEmpty() && !phone.isEmpty() && !email.isEmpty() && isImageSelected){
            ContactModel contactModel = new ContactModel(name, title, phone, email, image);
            return contactModel;
        }
        else{
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncherForSelectedImage.launch(intent);
        }
    }

    public void imageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncherForSelectedImage.launch(intent);
    }

    public void registerForActivityForSelectedImage() {
        activityResultLauncherForSelectedImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK && data != null) {
                            selectedImage = data.getData();
                            imageViewAddImage.setImageURI(selectedImage);
                            isImageSelected = true;
                        }//else(resultCode == RESULT_CANCELED) {
                        //isImageSelected = false;
                        // Toast.makeText(AddContactActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();//
                        // imageViewAddImage.setImageResource(R.drawable.baseline_person_add_24);}

                    }
                });
    }



}








