package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SetupActivity extends AppCompatActivity
{
    private EditText username_Setup, fullname_Setup;
    private Button saveBtn_Setup;
    private ImageView profile_Setup;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        username_Setup = findViewById(R.id.username_Setup);
        fullname_Setup = findViewById(R.id.fullname_Setup);
        saveBtn_Setup = findViewById(R.id.saveBtn_Setup);
        profile_Setup = findViewById(R.id.profile_Setup);

    }
}