package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddCategory extends AppCompatActivity
{
    private Button add_btn_id;
    private DatabaseReference CategoryRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        add_btn_id = findViewById(R.id.add_btn_id);
        CategoryRef = FirebaseDatabase.getInstance().getReference().child("ForumCategories");


        add_btn_id.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap categoryMap = new HashMap();
                categoryMap.put("Robbery","Robbery");
                categoryMap.put("Assault","Assault");
                categoryMap.put("Other","Other");

                CategoryRef.updateChildren(categoryMap);
            }
        });

    }
}