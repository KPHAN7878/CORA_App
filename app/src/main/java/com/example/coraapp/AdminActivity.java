package com.example.coraapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity
{

    private EditText user_text;
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
    }

    //method that goes to post activity
    private void SendUserToAllUsersActivity()
    {
        Intent newReportIntent = new Intent(AdminActivity.this, AllUsersActivity.class);
        startActivity(newReportIntent);
    }

}