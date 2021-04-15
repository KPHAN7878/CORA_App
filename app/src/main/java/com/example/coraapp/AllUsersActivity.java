package com.example.coraapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AllUsersActivity extends AppCompatActivity {

    private Query UsersRef;
    //recyclerview variables
    private RecyclerView userList;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        userList = (RecyclerView) findViewById(R.id.all_users);
        userList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        userList.setLayoutManager(linearLayoutManager);

        DisplayAllUsers();
    }


    //method that displays all posts
    //using firebse recycle adapter
    private void DisplayAllUsers()
    {
        super.onStart();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()


                        .setQuery(UsersRef, new SnapshotParser<Users>()


                        {
                            @NonNull
                            @Override
                            public Users parseSnapshot(@NonNull DataSnapshot snapshot)
                            {
                                return new Users(snapshot.child("FullName").getValue().toString());
                            }
                        })

                        //.setQuery(PostsRef, Posts.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Users, AllUsersActivity.UsersViewHolder>(options)
        {
            @Override
            public AllUsersActivity.UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_list_layout, parent, false);

                return new AllUsersActivity.UsersViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(AllUsersActivity.UsersViewHolder holder, final int position, Users model)
            {
                //get unique for each occurrence post
                final String OccurrenceKey = getRef(position).getKey();

                holder.usersName.setText(model.getFullName());

            }
        };

        Log.d("aaaa", userList.toString());
        userList.setAdapter(adapter);

        adapter.startListening();
        //
    }

    //static class for "FirebseRecyclerAdapter" in method "DisplayAllPosts"
    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        TextView usersName;

        //constructor
        public UsersViewHolder(@NonNull View itemView)
        {
            super(itemView);

            usersName = itemView.findViewById(R.id.User_user_name);
        }

    }
}