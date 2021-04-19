package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity
{
    //test from original pc
    //test 2

    private EditText username_Setup, fullname_Setup;
    private Button saveBtn_Setup;
    private ImageView profile_Setup;

    //database
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        //database stuff
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);


        username_Setup = findViewById(R.id.username_Setup);
        fullname_Setup = findViewById(R.id.fullname_Setup);
        saveBtn_Setup = findViewById(R.id.saveBtn_Setup);
        profile_Setup = findViewById(R.id.profile_Setup);

        saveBtn_Setup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveAccountSetupInfo();
            }
        });
    }

    //method to save account info
    private void SaveAccountSetupInfo()
    {
        String username = username_Setup.getText().toString();
        String fullname = fullname_Setup.getText().toString();

        //check if fields are empty
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //use hashmap to store data in firebase
            HashMap userMap = new HashMap();
            userMap.put("Username", username);
            userMap.put("FullName", fullname);

            //update in database
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    //if success display toast message
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SetupActivity.this, "Account Setup Complete", Toast.LENGTH_SHORT).show();

                        //send  user to main activity
                        sendUserToMainActivity();
                    }
                    //if failed display toast message and exception
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    //method that sends user to main activity after account setup successful
    private void sendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        //flag for if user setup is complete then they cannot go back via back button
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}