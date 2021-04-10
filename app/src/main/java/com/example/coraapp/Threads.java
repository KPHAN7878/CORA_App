package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Threads extends AppCompatActivity
{

    TextView test_text_id;

    private String testString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        test_text_id = findViewById(R.id.test_text_id);

        /** get string extra from previous */
        testString = getIntent().getExtras().get("Topic").toString();
        test_text_id.setText(testString);

    }
}