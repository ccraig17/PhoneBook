package com.craig.phonebook.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


import com.craig.phonebook.R;

public class MainActivity extends AppCompatActivity {
    private Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisplay = findViewById(R.id.buttonDisplay);
        btnDisplay.setOnClickListener(view -> {
            Intent intent = new Intent(this, ContactListActivity.class);
            startActivity(intent);

        });

    }

}




