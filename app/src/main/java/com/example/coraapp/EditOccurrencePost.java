package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class EditOccurrencePost extends AppCompatActivity
{

    private ImageView post_edit_image;
    private EditText post_edit_title, post_edit_description;
    private Spinner category_edit_spinner;
    private Button post_edit_submit;

    //variable to store post key from last activity
    private String ThisPostKey;

    //firebase variables
    private StorageReference ImageRef;
    private DatabaseReference ThisOccurrenceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_occurrence_post);

        //get the key of the selected post to edit
        ThisPostKey = getIntent().getExtras().get("EditPostKey").toString();

        //navigate to this post in firebase
        ThisOccurrenceRef = FirebaseDatabase.getInstance().getReference().child(ThisPostKey);

        post_edit_image = findViewById(R.id.post_edit_image);
        post_edit_title = findViewById(R.id.post_edit_title);
        post_edit_description = findViewById(R.id.post_edit_description);
        category_edit_spinner = findViewById(R.id.category_edit_spinner);

        //
        ThisOccurrenceRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String edit_title = snapshot.child("title").getValue().toString();
                    String edit_description = snapshot.child("description").getValue().toString();
                    String edit_category = snapshot.child("category").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });




    }
}