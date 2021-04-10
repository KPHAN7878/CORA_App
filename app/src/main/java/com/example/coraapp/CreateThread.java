package com.example.coraapp;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class CreateThread extends AppCompatActivity
{
    //toolbar variables
    private ImageView back_button, post_button;
    private TextView toolbar_text;

    //thread data variables
    private EditText thread_title, thread_description;
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
    private DatabaseReference UserRef, ThreadsRef;
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
        ThreadsRef = FirebaseDatabase.getInstance().getReference().child(TopicString + "Threads");


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
        thread_submit.setOnClickListener(new View.OnClickListener()
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
        Calendar obtainDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = ImageRef.child("occurrence image").child(imageUri.getLastPathSegment() + postRandomID + ".jpg");

        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        storageURL = uri.toString();




                        //this part is retrieving user's name from database to link with their post
                        UserRef.child(userID).addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists())
                                {
                                    String fullname = snapshot.child("FullName").getValue().toString();
                                    String username = snapshot.child("Username").getValue().toString();


                                    //create new node in database for occurrence and store information
                                    // -store userID
                                    // - store date
                                    // - sotre title
                                    // - store storage url for image
                                    // - store full name
                                    HashMap occurrenceMap = new HashMap();
                                    occurrenceMap.put("UID", userID);
                                    occurrenceMap.put("date", saveCurrentDate);
                                    occurrenceMap.put("title", title);
                                    occurrenceMap.put("description", description);
                                    occurrenceMap.put("image", storageURL);
                                    occurrenceMap.put("FullName", fullname);
                                    occurrenceMap.put("Username", username);

                                    //add new occurrence reports to firebase under "Occurrence" node and assign unique ID for each post
                                    ThreadsRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Intent GoToThread = new Intent(CreateThread.this, Threads.class);
                                                startActivity(GoToThread);
                                                Toast.makeText(CreateThread.this, "Thank you", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(CreateThread.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });




                    }
                });


                //ReportInfoToDatabase();
            }
        });
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

        //this part is retrieving user's name from database to link with their post
        UserRef.child(userID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    //get user's full name
                    String fullname = snapshot.child("FullName").getValue().toString();
                    String username = snapshot.child("Username").getValue().toString();
                    storageURL = "null";


                    //create new node in database for occurrence and store information
                    // -store userID
                    // - store date
                    // - sotre title
                    // - store storage url for image
                    // - store full name
                    HashMap occurrenceMap = new HashMap();
                    occurrenceMap.put("UID", userID);
                    occurrenceMap.put("date", saveCurrentDate);
                    occurrenceMap.put("title", title);
                    occurrenceMap.put("description", description);
                    occurrenceMap.put("image", storageURL);
                    occurrenceMap.put("FullName", fullname);
                    occurrenceMap.put("Username", username);

                    //add new occurrence reports to firebase under "Occurrence" node and assign unique ID for each post
                    ThreadsRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Intent GoToThread = new Intent(CreateThread.this, Threads.class);
                                startActivity(GoToThread);
                                Toast.makeText(CreateThread.this, "Thank you", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(CreateThread.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

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