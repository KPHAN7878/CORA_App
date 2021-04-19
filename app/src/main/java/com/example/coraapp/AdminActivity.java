package com.example.coraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity
{

    private Button ban_user, remove_forum, remove_occurence;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ban_user = findViewById(R.id.ban_user);
        remove_forum = findViewById(R.id.remove_forum);
        remove_occurence = findViewById(R.id.remove_occurence);

        ban_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToAllUsersActivity();
            }

        });

        remove_occurence.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToMainAdminActivity();
            }

        });
    }

    //method that goes to all users activity
    private void SendUserToAllUsersActivity()
    {
        Intent newReportIntent = new Intent(AdminActivity.this, AllUsersActivity.class);
        startActivity(newReportIntent);
    }

    //method that goes to main activity
    private void SendUserToMainAdminActivity()
    {
        Intent newReportIntent = new Intent(AdminActivity.this, MainAdminActivity.class);
        startActivity(newReportIntent);
    }

}