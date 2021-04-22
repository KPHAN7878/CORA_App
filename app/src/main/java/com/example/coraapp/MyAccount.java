package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyAccount extends AppCompatActivity
{

    ImageView account_profile, back_button;

    TextView account_username, account_fullname, account_address;

    Button account_edit;


    private FirebaseAuth mAuth;
    private DatabaseReference MyAccountRef;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        MyAccountRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);



        account_profile = findViewById(R.id.account_profile);
        account_username = findViewById(R.id.account_username);
        account_fullname = findViewById(R.id.account_fullname);
        account_address = findViewById(R.id.account_address);

        account_edit = findViewById(R.id.account_edit);

        back_button = findViewById(R.id.back_button);

        //retireve data from firebase user instance to display
        MyAccountRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    String username = snapshot.child("Username").getValue().toString();
                    String fullname = snapshot.child("FullName").getValue().toString();
                    String profile = snapshot.child("Profile").getValue().toString();
                    String address = snapshot.child("Address").getValue().toString();

                    account_username.setText(username);
                    account_fullname.setText(fullname);
                    account_address.setText(address);

                    if(profile.equals("null"))
                    {

                    }
                    else
                    {
                        Picasso.get().load(profile).fit().centerCrop().into(account_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });





        account_edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent GoToAccountEdit = new Intent(MyAccount.this, EditAccount.class);
                GoToAccountEdit.putExtra("UserID", currentUser);
                startActivity(GoToAccountEdit);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent BackToHome = new Intent(MyAccount.this, MainActivity.class);
                startActivity(BackToHome);
            }
        });

    }
}