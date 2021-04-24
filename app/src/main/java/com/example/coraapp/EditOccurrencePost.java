package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class EditOccurrencePost extends AppCompatActivity
{

    private ImageView post_edit_image, back_button2;
    private EditText post_edit_title, post_edit_description;
    private Button post_edit_submit;

    //variable to store post key from last activity
    private String ThisPostKey;

    //firebase variables
    private StorageReference ImageRef;
    private DatabaseReference ThisOccurrenceRef, OccRef;

    //image variables
    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;
    private String OccImageURL;

    //variable to update counter for edited posts
    private long post_counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_occurrence_post);

        //get the key of the selected post to edit
        ThisPostKey = getIntent().getExtras().get("EditPostKey").toString();

        //navigate to this post in firebase
        ThisOccurrenceRef = FirebaseDatabase.getInstance().getReference().child("Occurrence").child(ThisPostKey);
        OccRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");

        //for storing image to firebase storage
        ImageRef = FirebaseStorage.getInstance().getReference();

        post_edit_image = findViewById(R.id.post_edit_image);
        post_edit_title = findViewById(R.id.post_edit_title);
        post_edit_description = findViewById(R.id.post_edit_description);
        post_edit_submit = findViewById(R.id.post_edit_submit);

        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(EditOccurrencePost.this, MainActivity.class);
                startActivity(Home);
            }
        });


        ThisOccurrenceRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String edit_title = snapshot.child("title").getValue().toString();
                    String edit_description = snapshot.child("description").getValue().toString();
                    String edit_image = snapshot.child("image").getValue().toString();


                    post_edit_title.setText(edit_title);
                    post_edit_description.setText(edit_description);

                    if(edit_image.equals("null"))
                    {

                    }
                    else
                    {
                        Picasso.get().load(edit_image).fit().centerCrop().into(post_edit_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        post_edit_image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });


        post_edit_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                //update counter
                OccRef.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            post_counter = snapshot.getChildrenCount();

                            HashMap CountOcc = new HashMap();
                            CountOcc.put("counter", post_counter);
                            ThisOccurrenceRef.updateChildren(CountOcc);

                            //String test_count;
                            //test_count = String.valueOf(post_counter);
                            //Toast.makeText(EditOccurrencePost.this, test_count, Toast.LENGTH_LONG).show();
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



                //get date time to use as unique key later if user updated profile image
                String saveCurrentDate;
                String saveCurrentTime;
                String postRandomID;

                Calendar obtainDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                saveCurrentDate = currentDate.format(obtainDate.getTime());

                Calendar obtainTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentTime = currentTime.format(obtainTime.getTime());

                postRandomID = saveCurrentDate + saveCurrentTime;


                if (imageUri != null)
                {
                    //store profile picture to firebase storage
                    StorageReference filePath = ImageRef.child("occurrence image").child(imageUri.getLastPathSegment() + postRandomID + ".jpg");

                    //storing and retrieving profile image from storage
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
                                    //get firebase storage link for profile picture
                                    OccImageURL = uri.toString();

                                    //adding profile link to firebase here because of bug that clears profilesotrage variable outside of this bracket
                                    HashMap profileMap = new HashMap();
                                    profileMap.put("image", OccImageURL);

                                    ThisOccurrenceRef.updateChildren(profileMap);

                                }
                            });
                        }
                    });
                }
                else
                {
                    OccImageURL = "null";
                }

                String submit_occ_title = post_edit_title.getText().toString();
                String submit_occ_description = post_edit_description.getText().toString();

                HashMap submitOcc = new HashMap();
                submitOcc.put("title", submit_occ_title);
                submitOcc.put("description", submit_occ_description);
                //submitOcc.put("counter", post_counter);

                ThisOccurrenceRef.updateChildren(submitOcc).addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        Intent GoToMyPosts = new Intent(EditOccurrencePost.this, MainActivity.class);
                        startActivity(GoToMyPosts);
                    }
                });

            }
        });




    }




    //method that gets image from gallery and displays on image button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK)
        {
            imageUri = data.getData();
            post_edit_image.setImageURI(imageUri);
        }
    }

}