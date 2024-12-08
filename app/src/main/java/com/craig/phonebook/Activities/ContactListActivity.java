package com.craig.phonebook.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.craig.phonebook.Adaptor;
import com.craig.phonebook.Model.ContactModel;
import com.craig.phonebook.DatabaseAccess;
import com.craig.phonebook.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class ContactListActivity extends AppCompatActivity {
    MaterialToolbar toolbarContactList;
    RecyclerView contactListRecyclerView;
    private FloatingActionButton fabAddContact;
    private Adaptor adaptor;
    private DatabaseAccess databaseAccess = new DatabaseAccess(this);
    private ArrayList<ContactModel> contactList = new ArrayList<>();
    private ActivityResultLauncher<Intent> activityResultLauncherForAddNewContact;
    private ConstraintLayout main;
    private ContactModel contactModelToDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        toolbarContactList = findViewById(R.id.toolbarContactList);
        setSupportActionBar(toolbarContactList);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        databaseAccess.open();
        fabAddContact = findViewById(R.id.fabAddContact);
        contactListRecyclerView = findViewById(R.id.contactListRecyclerView);
        main = findViewById(R.id.main);
        registrationForAddContactResult();

        adaptor = new Adaptor(contactList, ContactListActivity.this);
        contactListRecyclerView.setAdapter(adaptor);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddContact.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            activityResultLauncherForAddNewContact.launch(intent);
        });

        showContactList();
        adaptor = new Adaptor(contactList, this);
        contactListRecyclerView.setAdapter(adaptor);
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    contactList.remove(position) ;
                   //how to update the recyclerView when a contact is deleted.contactList.remove(position); // Remove the item at the specified positioncontactList.remove(position); // Remove the item at the specified positioncontactList.remove(position); // Remove the item at the specified positioncontactList.remove(position); // Remove the item at the specified position
                    databaseAccess.delete(contactList.get(position).getName());
                    adaptor.notifyDataSetChanged();
                    Snackbar.make(main, "Contact Deleted", Snackbar.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(contactListRecyclerView);
    }

    public void registrationForAddContactResult() {
        activityResultLauncherForAddNewContact = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                //check if the user is sending the data.
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();
                    if(resultCode == RESULT_OK && data != null){ //means the user had sent data from the AddContactActivity.
                        //transfer incoming data from the AddContactActivity to the ContactListActivity using keyword and object
                        String name = data.getStringExtra("name");
                        String title = data.getStringExtra("title");
                        String phone = data.getStringExtra("phone");
                        String email = data.getStringExtra("email");
                        byte[] image = data.getByteArrayExtra("image");
                        contactList.add(new ContactModel(name, title, phone, email, image));
                        adaptor.notifyDataSetChanged();
                        databaseAccess.insert(name,title,phone,email,image);
                    //SAVE DATA TO DATABASE HERE  databaseAccess.insert(name, title, phone, email, image);
                        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void showContactList(){
        Cursor cursor = databaseAccess.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()) {
                String name = cursor.getString(0);
                String title = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);
                byte[] image = cursor.getBlob(4);
                contactList.add(new ContactModel(name, title, phone, email, image));
                adaptor.notifyDataSetChanged();
            }
        }
    }


//public ArrayList<ContactModel> contactToAdd(ArrayList<ContactModel> contactModels, String name, String title, String phone, String email, String image){
//        ArrayList<ContactModel> contactModelList = new ArrayList<>();
//        contactModelList.add(new ContactModel(name, title, phone, email, image));
//        return contactModelList;
//}

//    private ArrayList<ContactModel> contactList(ArrayList<ContactModel> contactModels){
//            ArrayList<ContactModel> contactModelList = new ArrayList<>();
//            contactModelList.add(new ContactModel("Colin Craig", "Embryologist", "1111111111","colin.ocraig@gmail.com","me"));
//            contactModelList.add(new ContactModel("Curtis Craig", "Software Engineer",  "2222222222","curtis.ocraig@gmail.com","theboyz"));
//            contactModelList.add(new ContactModel("Nola Ogunyemi", "Senior Vice President", "333333333", "nola.ogunyemi@gmail.com", "nola"));
//        return contactModelList;
//    };
}