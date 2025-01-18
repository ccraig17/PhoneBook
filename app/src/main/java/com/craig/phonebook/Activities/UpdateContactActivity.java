package com.craig.phonebook.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.craig.phonebook.R;
import com.craig.phonebook.RoomDatabase.Contact;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateContactActivity extends AppCompatActivity {
    MaterialToolbar toolbarUpdateContact;
    private EditText editTxtUpdateName, editeTxtUpdateTitle, editTxtUpdatePhone, editTxtUpdateEmail;
    private int id;
    private String name, title, phone, email;
    private byte[]  image;
    private Button btnUpdateContact;
    CircleImageView imageViewUpdateImage;
    private FloatingActionButton btnUpdateImage;
    boolean isImageSelected = false;
    private ActivityResultLauncher<Intent> activityResultLauncherForSelectedImage;
    private Bitmap selectedImage;
    private Bitmap scaledImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        toolbarUpdateContact = findViewById(R.id.toolbarUpdateContact);
        toolbarUpdateContact.setNavigationOnClickListener(view -> {
            if (Objects.equals(toolbarUpdateContact.getTitle(), "Add Contact")) {
                startActivity(new Intent(this, ContactListActivity.class));
                finish();
            }
        });
        editTxtUpdateName = findViewById(R.id.editTxtUpdateName);
        editeTxtUpdateTitle = findViewById(R.id.editTxtTUpdateTitle);
        editTxtUpdatePhone = findViewById(R.id.editTxtUpdatePhone);
        editTxtUpdateEmail = findViewById(R.id.editTxtUpdateEmail);
        imageViewUpdateImage = findViewById(R.id.imageViewUpdateImage);
        btnUpdateImage = findViewById(R.id.btnCamera);
        btnUpdateContact = findViewById(R.id.buttonUpdateContact); //change to R.id.buttonUpdateContact
        //registerForActivity();
        registrationForSelectedImage();

        //receive the data from the ContactListActivity;
        // the selected contact's data is sent from the ContactListActivity to the UpdateContactActivity
        id = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        title = getIntent().getStringExtra("title");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        image = getIntent().getByteArrayExtra("image");

        //set the data to the EditText of the UpdateContactActivity
        editTxtUpdateName.setText(name);
        editeTxtUpdateTitle.setText(title);
        editTxtUpdatePhone.setText(phone);
        editTxtUpdateEmail.setText(email);
        imageViewUpdateImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

        btnUpdateImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                permission();
            }
            imageViewUpdateImage.setImageBitmap(selectedImage);
        });

        btnUpdateContact.setOnClickListener(v -> {
            updateData();
        });
    }

    public void updateData() {
        if(id == -1){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            return;
        }else{
            String updateName = editTxtUpdateName.getText().toString().trim();
            String updateTitle = editeTxtUpdateTitle.getText().toString().trim();
            String updatePhone = editTxtUpdatePhone.getText().toString().trim();
            String updateEmail = editTxtUpdateEmail.getText().toString().trim();

            Intent intent = new Intent(this, ContactListActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("updateName", updateName);
            intent.putExtra("updateTitle", updateTitle);
            intent.putExtra("updatePhone ", updatePhone );
            intent.putExtra("updateEmail", updateEmail);

            if (selectedImage == null) {
                //means the User did NOT select a new Image; return the original image
               intent.putExtra("image", image);
            } else {
                //the user has selected a new image
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                scaledImage = makeSmall(selectedImage, 300); //scaled the selected image to be passed to  the ContactList
                scaledImage.compress(Bitmap.CompressFormat.PNG,50, outputStream);
                byte[] image = outputStream.toByteArray();
                intent.putExtra("image", image);
            }
            setResult(RESULT_OK, intent);
            finish();
        }

    }
//    private void registerForActivity() {
//        activityResultLauncherForUpdateContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//
//            }
//        });
//    }


    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void permission() {
        String permission;
        if (Build.VERSION.SDK_INT >= 33) {
            permission = Manifest.permission.READ_MEDIA_IMAGES;
        } else if(Build.VERSION.SDK_INT >= 30){
            permission = Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;
        }
        else{
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }
        if (ContextCompat.checkSelfPermission(UpdateContactActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateContactActivity.this, new String[]{permission}, 1);
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
                        if(resultCode == RESULT_OK && data != null){
                            isImageSelected = true;
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                                imageViewUpdateImage.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            isImageSelected = false;
                            Toast.makeText(UpdateContactActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                            imageViewUpdateImage.setImageResource(R.drawable.baseline_person_add_24);
                        }
                    }
                }
        );
    }

    public Contact addContact(String name, String title, String phone, String email, byte[] image) {
        if (!name.isEmpty() && !title.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            return new Contact(name, title, phone, email, image);
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    public Bitmap makeSmall(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width /height;

        if(ratio >1){
            width = maxSize;
            height = (int) (width/ratio);
        }else{
            height = maxSize;
            width = (int) (height * ratio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }

}

