package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateProfile extends AppCompatActivity {

    private EditText userName;
    private EditText password;
    private EditText email;
    private EditText address;
    private Button saveBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        userName = findViewById(R.id.updateUsername);
        password = findViewById(R.id.updatePassword);
        email = findViewById(R.id.updateEmail);
        address = findViewById(R.id.updateAddress);
        saveBtn = findViewById(R.id.button_save);

        //set Custom hints based on user
        userName.setHint(mUser.getDisplayName());
        password.setHint("New Password");
        email.setHint(mUser.getEmail());
        address.setHint("Update Address");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    protected void updateProfile()
    {
        String name = userName.getText().toString()
                ,newPassword = password.getText().toString()
                ,newEmail = email.getText().toString()
                ,newAddress = address.getText().toString();

       if(!(TextUtils.isEmpty(name)))
       {
           //Update name here
       }
       if(!(TextUtils.isEmpty(newPassword)))
       {
           mUser.updatePassword(password.getText().toString());
       }
       if(!(TextUtils.isEmpty(newEmail)))
       {
           mUser.updateEmail(email.getText().toString());
       }
       if(!(TextUtils.isEmpty(newAddress)))
       {
           //update address here
       }
        Toast.makeText(this, "Account Updated", Toast.LENGTH_SHORT).show();
        Intent returnIntent = new Intent(UpdateProfile.this,MainActivity.class);
        startActivity(returnIntent);
        finish();
    }
}