package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class EditAccount extends AppCompatActivity
{

    private String UserIDString;
    private DatabaseReference AccountRef, EditRef;
    private FirebaseAuth mAuth;
    private StorageReference ImageRef;

    ImageView edit_profile, back_button2;
    EditText edit_username, edit_fullname, edit_address, edit_city, edit_zipcode;
    Button edit_submit;

    //for updating profile picture
    String storageURL;
    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    //variable to hold updated profile link
    private String profileStorageURL;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        //get user id from previous activity
        UserIDString = getIntent().getExtras().get("UserID").toString();


        edit_profile = findViewById(R.id.edit_profile);

        edit_username = findViewById(R.id.edit_username);
        edit_fullname = findViewById(R.id.edit_fullname);
        edit_address = findViewById(R.id.edit_address);
        edit_city = findViewById(R.id.edit_city);
        edit_zipcode = findViewById(R.id.edit_zipcode);

        edit_submit = findViewById(R.id.edit_submit);

        back_button2 = findViewById(R.id.back_button2);

        back_button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent Home = new Intent(EditAccount.this, MainActivity.class);
                startActivity(Home);
            }
        });


        //get the data from that particular user
        AccountRef = FirebaseDatabase.getInstance().getReference().child("Users");
        EditRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserIDString);


        //for storing image to firebase storage
        ImageRef = FirebaseStorage.getInstance().getReference();


        AccountRef.child(UserIDString).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String username = snapshot.child("Username").getValue().toString();
                    String fullname = snapshot.child("FullName").getValue().toString();
                    String profile = snapshot.child("Profile").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();
                    String city = snapshot.child("City").getValue().toString();
                    String zipcode = snapshot.child("Zipcode").getValue().toString();

                    edit_username.setText(username);
                    edit_fullname.setText(fullname);
                    edit_address.setText(address);
                    edit_city.setText(city);
                    edit_zipcode.setText(zipcode);
                    //Picasso.get().load(profile).into(edit_profile);

                    if(profile.equals("null"))
                    {

                    }
                    else
                    {
                        Picasso.get().load(profile).fit().centerCrop().into(edit_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener()
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

        edit_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /** get date time to use as unique key later if user updated profile image */
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
                    StorageReference filePath = ImageRef.child("profile images").child(imageUri.getLastPathSegment() + postRandomID + "-profile" + ".jpg");

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
                                    profileStorageURL = uri.toString();

                                    /** adding profile link to firebase here because of bug that clears profilesotrage variable outside of this bracket */
                                    HashMap profileMap = new HashMap();
                                    profileMap.put("Profile", profileStorageURL);

                                    EditRef.updateChildren(profileMap);

                                }
                            });
                        }
                    });
                }
                else
                {
                    profileStorageURL = "null";
                }

                //submit all other edited information

                String submit_username = edit_username.getText().toString();
                String submit_fullname = edit_fullname.getText().toString();
                String submit_address = edit_address.getText().toString();
                String submit_city = edit_city.getText().toString();
                String submit_zipcode = edit_zipcode.getText().toString();



                HashMap editMap = new HashMap();
                editMap.put("Username", submit_username);
                editMap.put("FullName", submit_fullname);
                editMap.put("Address", submit_address);
                editMap.put("City", submit_city);
                editMap.put("Zipcode", submit_zipcode);

                EditRef.updateChildren(editMap).addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        //return back to account view profile once done
                        Intent GoToMyAccount = new Intent(EditAccount.this, MyAccount.class);
                        startActivity(GoToMyAccount);
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
            edit_profile.setImageURI(imageUri);
        }
    }
}