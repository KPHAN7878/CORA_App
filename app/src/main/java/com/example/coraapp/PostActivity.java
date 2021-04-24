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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{


    //tutorial21 branch test comment
    //push to master after merging master with tutorial21

    //SavePostInfo branch test comment

    /** post data variables */
    private Button submit_btn_post;
    private Button location_button;
    private EditText Title_post;
    private EditText description_post;
    private ImageButton picture_post;
    private Spinner spinner;
    private ImageView back_button2;

    /** coordinate variables */
    String lat;
    String lng;
    String zipcode;
    String city;

    /** spinner array */
    String[] category = {"Category", "Theft", "Burglary", "Assault", "Murder", "Other"};
    private String crime = "";


    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    //string that stores editText post info
    private String title;
    private String description;
    private String profilePic;


    //firebase variables
    private StorageReference ImageRef;
    private DatabaseReference UserRef, OccurrenceRef;
    private FirebaseAuth mAuth;


    //time variables to create unique image name
    private String saveCurrentDate, saveCurrentTime, postRandomID;
    String storageURL;
    private String userID;


    private Button test_btn;

    //post counter for sorting
    private long post_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //references to user and occurrence in database
        //reference to image in storage
        ImageRef = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        OccurrenceRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");

        //initialize
        submit_btn_post = findViewById(R.id.submit_btn_post);
        Title_post = findViewById(R.id.Title_post);
        description_post = findViewById(R.id.description_post);
        picture_post = findViewById(R.id.picture_post);
        location_button = findViewById(R.id.location_button);


        //initialize spinner with category string array
        spinner = findViewById(R.id.spinner_post);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(PostActivity.this, MainActivity.class);
                startActivity(Home);
            }
        });


        /*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        */

        //when picture button is clicked
        picture_post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();

            }
        });

        //listener to acquire location
        location_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent mapIntent = new Intent(PostActivity.this, GetLocation.class);
                startActivity(mapIntent);
            }
        });

        /** this part is to get the long and lat for the occurrence post*/
        try
        {
            Bundle extras = getIntent().getExtras();
            if(extras != null)
            {
                lat = extras.getString("lat");
                lng = extras.getString("lng");
                zipcode = extras.getString("zip");
                city = extras.getString("city");

            }
        }
        catch(Exception e)
        {

        }

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
        description = description_post.getText().toString();

        if(crime.equals("Category"))
        {
            Toast.makeText(this, "Please Choose A Category", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(title))
        {
            Toast.makeText(this, "Please Enter a Title", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(lng) && TextUtils.isEmpty(lat))
        {
            Toast.makeText(this, "Please Choose Location", Toast.LENGTH_SHORT).show();
        }

        /**
        else if(!Uri.EMPTY.equals(imageUri))
        {
            NoImageToFirebase();
        }
        */
        else if(imageUri == null)
        {
            NoImageToFirebase();
        }

        else
        {
            StoreImageToFirebaseStorage();
        }
    }




    //if the user does not submit an image
    private void NoImageToFirebase()
    {
        //create post counter
        OccurrenceRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    post_counter = snapshot.getChildrenCount();
                }
                else
                {
                    post_counter = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        Calendar obtainDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
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
                    String fullname = snapshot.child("FullName").getValue().toString();
                    String USERNAME = snapshot.child("Username").getValue().toString();
                    storageURL = "null";

                    //get profile pic
                    UserRef.child(userID).addValueEventListener(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            if(snapshot.exists())
                            {
                                profilePic = snapshot.child("Profile").getValue().toString();
                                HashMap occurrence_profile = new HashMap();
                                occurrence_profile.put("ProfilePic", profilePic);
                                OccurrenceRef.child(userID + postRandomID).updateChildren(occurrence_profile);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });

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
                    occurrenceMap.put("category", crime);
                    occurrenceMap.put("FullName", fullname);
                    occurrenceMap.put("Username", USERNAME);
                    occurrenceMap.put("latitude", lat);
                    occurrenceMap.put("longitude", lng);
                    occurrenceMap.put("zipcode", zipcode);
                    occurrenceMap.put("city", city);
                    occurrenceMap.put("counter", post_counter);
                    //occurrenceMap.put("ProfilePic", profilePic);

                    //add new occurrence reports to firebase under "Occurrence" node and assign unique ID for each post
                    OccurrenceRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(PostActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    


    //if user chooses to upload image
    private void StoreImageToFirebaseStorage()
    {

        //create post counter
        OccurrenceRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    post_counter = snapshot.getChildrenCount();
                }
                else
                {
                    post_counter = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        Calendar obtainDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;

        //store image to firebase
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
                                    String USERNAME = snapshot.child("Username").getValue().toString();


                                    //get profile pic
                                    UserRef.child(userID).addValueEventListener(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            if(snapshot.exists())
                                            {
                                                profilePic = snapshot.child("Profile").getValue().toString();
                                                HashMap occurrence_profile = new HashMap();
                                                occurrence_profile.put("ProfilePic", profilePic);
                                                OccurrenceRef.child(userID + postRandomID).updateChildren(occurrence_profile);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error)
                                        {

                                        }
                                    });


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
                                    occurrenceMap.put("category", crime);
                                    occurrenceMap.put("FullName", fullname);
                                    occurrenceMap.put("Username", USERNAME);
                                    occurrenceMap.put("latitude", lat);
                                    occurrenceMap.put("longitude", lng);
                                    occurrenceMap.put("zipcode", zipcode);
                                    occurrenceMap.put("city", city);
                                    occurrenceMap.put("counter", post_counter);

                                    //add new occurrence reports to firebase under "Occurrence" node and assign unique ID for each post
                                    OccurrenceRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                SendUserToMainActivity();
                                                Toast.makeText(PostActivity.this, "Thank you", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(PostActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

            }
        });
    }


    //method to redirect user back to main activity after reporting occurrence
    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(PostActivity.this, MainActivity.class);
        startActivity(mainIntent);
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




    //1. method 1 for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        crime = category[position];

    }

    //2. method 2 for spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}