package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private Button submit_btn_post;
    private EditText Title_post;
    private ImageButton picture_post;

    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    //string that stores title of post
    private String title;

    //firebase variables
    private StorageReference ImageRef;

    //time variables to create unique image name
    private String saveCurrentDate, saveCurrentTime, postRandomID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ImageRef = FirebaseStorage.getInstance().getReference();

        submit_btn_post = findViewById(R.id.submit_btn_post);
        Title_post = findViewById(R.id.Title_post);
        picture_post = findViewById(R.id.picture_post);

        //when picture button is clicked
        picture_post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });


        //when submit button is clicked
        submit_btn_post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InitiatePost();
            }
        });
    }


    //method that starts the post process
    private void InitiatePost()
    {
        title = Title_post.getText().toString();

        if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this, "Please Enter a Title", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreImageToFirebaseStorage();
        }
    }


    //method that actually stores the image to firebase storage
    private void StoreImageToFirebaseStorage()
    {
        Calendar obtainDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = ImageRef.child("occurrence image").child(imageUri.getLastPathSegment() + postRandomID + ".jpg");

        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(PostActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //method to open gallery and select image
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
            picture_post.setImageURI(imageUri);
        }
    }
}