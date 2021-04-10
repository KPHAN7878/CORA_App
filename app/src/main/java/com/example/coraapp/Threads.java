package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Threads extends AppCompatActivity
{
    private ImageView back_button, post_button;
    private TextView toolbar_text;


    private String testString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        back_button = findViewById(R.id.back_button);
        post_button = findViewById(R.id.post_button);
        toolbar_text = findViewById(R.id.toolbar_text);


        /** get string extra from previous */
        testString = getIntent().getExtras().get("Topic").toString();

        toolbar_text.setText(testString);

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent backIntent = new Intent(Threads.this, ForumsHome.class);
                startActivity(backIntent);
            }
        });

        post_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent postIntent = new Intent(Threads.this, CreateThread.class);
                postIntent.putExtra("Topic", testString);
                startActivity(postIntent);
            }
        });


    }
}