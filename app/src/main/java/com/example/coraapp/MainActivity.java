package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    private Button logoutMain;
    private FloatingActionButton report_btn_main;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutMain = findViewById(R.id.logoutMain);
        report_btn_main = findViewById(R.id.report_btn_main);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //logout button test
        logoutMain.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mAuth.signOut();
                SendUserToLoginActivity();
            }
        });

        report_btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToPostActivity();
            }
        });
    }

    private void SendUserToPostActivity()
    {
        Intent newReportIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(newReportIntent);
    }


    //when app is opened make sure that the user is logged in
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }

        //check if user exists in database
        else
        {
            CheckUserExistence();
        }
    }



    //method that checks if user is in database
    private void CheckUserExistence()
    {
        final String currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //MIGHT NEED TO EDIT THIS LATER - TUTORIAL 12 BUG

                //if user is authenticated (email and password) but is not in database yet (name, username, profile, etc)
                //if this is the case send user to setup page
                if(!snapshot.hasChild(currentUserID))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }




    //method that sends user to setup activity
    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }



    //login validation method
    //if user is not logged in then redirect to login screen
    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}