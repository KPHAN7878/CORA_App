package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class EditOccurrencePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener
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

    /** spinner array */
    private String[] category = {"Category", "Theft", "Burglary", "Assault", "Murder", "Suspicious Activity"};
    private String crime = "";
    private String current_cateogry;

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
                    String edit_image = snapshot.child("image").getValue().toString();

                    current_cateogry = edit_category;


                    post_edit_title.setText(edit_title);
                    post_edit_description.setText(edit_description);
                    post_edit_title.setText(edit_title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        //initialize spinner with category string array
        category_edit_spinner = findViewById(R.id.category_edit_spinner);
        category_edit_spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_edit_spinner.setAdapter(adapter);
        category_edit_spinner.setPrompt(current_cateogry);






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