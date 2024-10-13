package com.craig.phonebook;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarAddContact;
    private EditText editTxtName, editeTxtTitle, editTxtPhone, editTxtEmail;
    private Button btnSaveContact;
    CircleImageView imageViewAddImage;
    boolean isImageSelected = false;
    private Uri selectedImage;
    private ArrayList<ContactModel> contactList = new ArrayList<>();
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
            Uri imageUri = Uri.parse(selectedImage.toString());
            if (name.isEmpty() || title.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isImageSelected) {
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("title", title);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("image", imageUri.toString());
               contactList.add(new ContactModel(name, title, phone, email, imageUri));
                activityResultLauncherForSelectedImage.launch(intent);
                startActivity(new Intent(this, ContactListActivity.class));
                Toast.makeText(this, "Contact Saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                addContact(name, title, phone, email, null);
                Toast.makeText(this, "No image selected, please select an image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*
     * these Methods will be passed to the btnSaveContact when clicked.
     * review how to get Images, converted selected image to Uri image
     * pass Uri selectedImage to Add ContactActivity
     * */
    public ContactModel addContact(String name, String title, String phone, String email, Uri image) {
        if (!name.isEmpty() && !title.isEmpty() && !phone.isEmpty() && !email.isEmpty() && image != null) {
            return new ContactModel(name, title, phone, email, image);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public void imageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncherForSelectedImage.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("images/*");
            activityResultLauncherForSelectedImage.launch(intent);
        }
    }

    public void registerForActivityForSelectedImage() {
        activityResultLauncherForSelectedImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if (resultCode == RESULT_OK && data != null) {
                            String name = data.getStringExtra("name");
                            String title = data.getStringExtra("title");
                            String phone = data.getStringExtra("phone");
                            String email = data.getStringExtra("email");
                            Uri selectedImage = Uri.parse(data.getStringExtra("image"));
                            Picasso.get().load(selectedImage).into(imageViewAddImage);
                            imageViewAddImage.setImageURI(selectedImage);
                            contactList.add(new ContactModel(name, title, phone, email, selectedImage));
                            isImageSelected = true;
                        } else {
                            isImageSelected = false;
                            Toast.makeText(AddContactActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            imageViewAddImage.setImageResource(R.drawable.baseline_person_add_24);
                        }
                    }


                });
    }
}











