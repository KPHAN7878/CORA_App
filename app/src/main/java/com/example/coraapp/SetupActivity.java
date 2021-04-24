package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SetupActivity extends AppCompatActivity
{
    //test from original pc
    //test 2

    private EditText username_Setup, fullname_Setup, address_Setup, city_Setup, zipcode_Setup;
    private Button saveBtn_Setup;
    private ImageView profile_Setup;

    //database
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference ImageRef;
    String currentUserID;

    //geocoder variable
    private String latString;
    private String langString;


    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;

    //variable that gets firebase storage profile url
    private String profileStorageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        //database stuff
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        //for storing image to firebase storage
        ImageRef = FirebaseStorage.getInstance().getReference();


        username_Setup = findViewById(R.id.username_Setup);
        fullname_Setup = findViewById(R.id.fullname_Setup);
        saveBtn_Setup = findViewById(R.id.saveBtn_Setup);
        profile_Setup = findViewById(R.id.profile_Setup);
        address_Setup = findViewById(R.id.address_Setup);
        city_Setup = findViewById(R.id.city_Setup);
        zipcode_Setup = findViewById(R.id.zipcode_Setup);

        //upload profile picture
        profile_Setup.setOnClickListener(new View.OnClickListener()
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

        //submit info
        saveBtn_Setup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SaveAccountSetupInfo();
            }
        });
    }

    //method to save account info
    private void SaveAccountSetupInfo()
    {
        String username = username_Setup.getText().toString();
        String fullname = fullname_Setup.getText().toString();
        String address = address_Setup.getText().toString();
        String city = city_Setup.getText().toString();
        String Zipcode = zipcode_Setup.getText().toString();

        /** variable that saves latlng string form */
        String latlngString;
        String latlngStrArr[];

        /** get date time to use as unique key later */
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

                            UsersRef.updateChildren(profileMap);

                        }
                    });
                }
            });
        }
        else
        {
            profileStorageURL = "null";
        }



        /** convert address string to lat lang coordinate */


        Geocoder geocoder = new Geocoder(this);
        List<Address> addy;
        LatLng coordinate = null;

        try
        {
            addy = geocoder.getFromLocationName(address, 5);

            Address location = addy.get(0);
            coordinate = new LatLng(location.getLatitude(), location.getLongitude());

            latlngString = coordinate.toString();
            latlngStrArr = latlngString.split(",");
            latString = latlngStrArr[0];
            langString = latlngStrArr[1];
        }
        catch (IOException ex)
        {
            Log.e("LocateMe", "Could not get geocoder data", ex);
        }


        /** convert address string to lat lang ends here */



        //check if fields are empty
        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //use hashmap to store data in firebase
            HashMap userMap = new HashMap();
            userMap.put("Username", username);
            userMap.put("FullName", fullname);
            userMap.put("Profile", profileStorageURL);
            userMap.put("Address", address);
            userMap.put("City", city);
            userMap.put("Zipcode", Zipcode);


            userMap.put("Latitude", latString);
            userMap.put("Longitude", langString);
            userMap.put("coordinate", coordinate);
            userMap.put("Admin", "NO");


            //update in database
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    //if success display toast message
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SetupActivity.this, "Account Setup Complete", Toast.LENGTH_SHORT).show();

                        //send  user to main activity
                        sendUserToMainActivity();
                    }
                    //if failed display toast message and exception
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //method that gets image from gallery and displays on image button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK)
        {
            imageUri = data.getData();
            profile_Setup.setImageURI(imageUri);
        }
    }


    //method that sends user to main activity after account setup successful
    private void sendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        //flag for if user setup is complete then they cannot go back via back button
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}