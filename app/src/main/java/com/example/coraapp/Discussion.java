package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Discussion extends AppCompatActivity
{

    private String getThreadString;
    private String getTopicString;

    //firebase variables
    private DatabaseReference threadRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        /** get string extra from previous */
        getThreadString = getIntent().getExtras().get("ThreadKey").toString();
        getTopicString = getIntent().getExtras().get("Topic").toString();

        //get instance of the specific thread
        threadRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString);

    }
}