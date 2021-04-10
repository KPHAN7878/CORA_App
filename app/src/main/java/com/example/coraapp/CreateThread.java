package com.example.coraapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class CreateThread extends AppCompatActivity
{
    //toolbar variables
    private ImageView back_button, post_button;
    private TextView toolbar_text;

    //thread data variables
    private TextView thread_title, thread_description;
    private ImageView thread_image;
    private Button thread_submit;

    //variables to store thread title and description
    private String title;
    private String description;

    //date and time variables to generate unique image name for storage
    private String saveCurrentDate, saveCurrentTime, postRandomID;
    String storageURL;
    private String userID;

    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    //firebase variables
    private StorageReference ImageRef;
    private DatabaseReference UserRef, OccurrenceRef;
    private FirebaseAuth mAuth;

    /**variable to getExtra from previous*/
    private String TopicString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);

        /**getExtra from previous*/
        TopicString = getIntent().getExtras().get("Topic").toString();


        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //references to user and occurrence in database
        //reference to image in storage
        ImageRef = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        OccurrenceRef = FirebaseDatabase.getInstance().getReference().child(TopicString + "Threads");


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

        //listener for submit thread button
        post_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InitiatePost();
            }
        });
    }

    //method to initialize thread post data
    private void InitiatePost()
    {
        title = thread_title.getText().toString();
        description = thread_description.getText().toString();

        if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this, "Please Enter a Title", Toast.LENGTH_SHORT).show();
        }
        else if(imageUri == null)
        {
            NoImageToFirebase();
        }
        else
        {
            StoreImageToFirebaseStorage();
        }
    }

    private void StoreImageToFirebaseStorage()
    {
    }

    //if the user does nsot submit an image
    private void NoImageToFirebase()
    {
        Calendar obtainDate = Calendar.getInstance();
        android.icu.text.SimpleDateFormat currentDate = new android.icu.text.SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        android.icu.text.SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;
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