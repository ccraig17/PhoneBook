package com.craig.phonebook.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.craig.phonebook.Model.ContactModel;
import com.craig.phonebook.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarAddContact;
    private EditText editTxtName, editeTxtTitle, editTxtPhone, editTxtEmail;
    private Button btnSaveContact;
    private FloatingActionButton btnCamera;
    CircleImageView imageViewAddImage;
    boolean isImageSelected = false;
    private Bitmap selectedImage;
    private ActivityResultLauncher<Intent> activityResultLauncherForSelectedImage;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)

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
        btnCamera = findViewById(R.id.fab_Camera);
        btnSaveContact = findViewById(R.id.buttonSaveContact);
        registrationForSelectedImage();

        btnCamera.setOnClickListener(view -> {
            permission();
            imageViewAddImage.setImageBitmap(selectedImage);
        });

        btnSaveContact.setOnClickListener(view -> {
            if (selectedImage == null) {//if used presses the save btn w/o selecting and image => app crashes
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            } else {
                imageViewAddImage.setImageBitmap(selectedImage);
                String name = editTxtName.getText().toString();
                String title = editeTxtTitle.getText().toString();
                String phone = editTxtPhone.getText().toString();
                String email = editTxtEmail.getText().toString();
                String image = selectedImage.toString();

                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("title", title);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("image", image);
                setResult(RESULT_OK, intent);
                finish();

            }

        });

    }

    private void permission() {
        String permission;
        if (Build.VERSION.SDK_INT >= 33) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (ContextCompat.checkSelfPermission(AddContactActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddContactActivity.this, new String[]{permission}, 1);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectedImage.launch(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectedImage.launch(intent);
        }
    }

    public void registrationForSelectedImage() {
        activityResultLauncherForSelectedImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null && data.getData() != null){
                            isImageSelected = true;
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                imageViewAddImage.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            isImageSelected = false;
                            Toast.makeText(AddContactActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                            imageViewAddImage.setImageResource(R.drawable.baseline_person_add_24);
                        }
                    }
                }
        );
    }

    public ContactModel addContact(String name, String title, String phone, String email) {
        if (!name.isEmpty() && !title.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            return new ContactModel(name, title, phone, email);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}











