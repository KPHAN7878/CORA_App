package com.example.coraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateThread extends AppCompatActivity
{
    //toolbar variables
    private ImageView back_button, post_button;
    private TextView toolbar_text;

    //thread data variables
    private TextView thread_title, thread_description;
    private ImageView thread_image;
    private Button thread_submit;

    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);

        //initialize toolbar variables
        back_button = findViewById(R.id.back_button);
        post_button = findViewById(R.id.post_button);
        toolbar_text = findViewById(R.id.toolbar_text);

        //initialize thread info variables
        thread_title = findViewById(R.id.thread_title);
        thread_description = findViewById(R.id.thread_description);
        thread_image = findViewById(R.id.thread_image);
        thread_submit = findViewById(R.id.thread_submit);

        toolbar_text.setText("Create Thread");
        post_button.setVisibility(View.INVISIBLE);

        //listener for tool bar back button
        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent backIntent = new Intent(CreateThread.this, Threads.class);
                startActivity(backIntent);
            }
        });

        //listener for upload image
        thread_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });
    }

    //function to upload image
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    //method that gets image from gallery and displays on image button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK)
        {
            imageUri = data.getData();
            thread_image.setImageURI(imageUri);
        }
    }
}