package com.craig.phonebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarAddContact;
    private EditText editTxtName, editeTxtTitle, editTxtPhone, editTxtEmail;
    private Button btnSaveContact;
    CircleImageView imageViewAddImage;
    boolean isImageSelected = false;
    private Bitmap selectedImage;
    private Bitmap scaledImage;
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
            String permission;
            if(Build.VERSION.SDK_INT >= 33){
                permission = Manifest.permission.READ_MEDIA_IMAGES;
            }else{
                permission = Manifest.permission.READ_EXTERNAL_STORAGE;
            }
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
               // requestPermissions(new String[]{permission}, 1);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForSelectedImage.launch(intent);
            }

        });
        btnSaveContact.setOnClickListener(view -> {
            if(selectedImage == null){//if used presses the save btn w/o selecting and image => app crashes
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }else{
                String name = editTxtName.getText().toString();
                String title = editeTxtTitle.getText().toString();
                String phone = editTxtPhone.getText().toString();
                String email = editTxtEmail.getText().toString();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makeSmall(selectedImage, 100);
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] image = outputStream.toByteArray();

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


    public ContactModel addContact(String name, String title, String phone, String email) {
        if (!name.isEmpty() && !title.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            return new ContactModel(name, title, phone, email);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
//    public void imageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        activityResultLauncherForSelectedImage.launch(intent);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                imageViewAddImage.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Picasso.get().load(selectedImage.toString()).into(imageViewAddImage);
                            isImageSelected = true;
                        } else {
                            isImageSelected = false;
                            Toast.makeText(AddContactActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            imageViewAddImage.setImageResource(R.drawable.baseline_person_add_24);
                        }
                    }


                });
    }
    public Bitmap makeSmall(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float) width / (float) height;
        if(width > height){
            width = maxSize;
            height = (int) (width / ratio);
        }else{
            height = maxSize;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}











