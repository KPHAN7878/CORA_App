package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MyPosts extends AppCompatActivity
{

    private RecyclerView myposts_recycler;
    private FirebaseRecyclerAdapter adapter;


    private FirebaseAuth mAuth;
    private DatabaseReference mpRef;
    //private DatabaseReference PostsRef;
    private Query MyPostsRef;

    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        myposts_recycler = (RecyclerView) findViewById(R.id.myposts_recycler);
        myposts_recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myposts_recycler.setLayoutManager(linearLayoutManager);


        DisplayMyPosts();
    }




    private void DisplayMyPosts()
    {
        super.onStart();

        /** get ID of current user */
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        mpRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");
        MyPostsRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");

        /** query for occurrence posts that contain the current user id to display */
        Query MyPostsQuery = mpRef.orderByChild("UID").startAt(currentUserID).endAt(currentUserID + "\uf8ff");

        FirebaseRecyclerOptions<MyPostsModel> options =
                new FirebaseRecyclerOptions.Builder<MyPostsModel>()


                        .setQuery(MyPostsQuery, new SnapshotParser<MyPostsModel>()


                        {
                            @NonNull
                            @Override
                            public MyPostsModel parseSnapshot(@NonNull DataSnapshot snapshot)
                            {
                                return new MyPostsModel(snapshot.child("FullName").getValue().toString(),
                                        snapshot.child("UID").getValue().toString(),
                                        snapshot.child("date").getValue().toString(),
                                        snapshot.child("image").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("description").getValue().toString(),
                                        snapshot.child("category").getValue().toString());
                            }
                        })

                        //.setQuery(PostsRef, Posts.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<MyPostsModel, MyPosts.MyPostsViewHolder>(options)
        {
            @Override
            public MyPosts.MyPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.myposts_template, parent, false);

                return new MyPosts.MyPostsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(MyPosts.MyPostsViewHolder holder, final int position, MyPostsModel model)
            {
                //get unique user's post when they click to edit
                final String SelectedMyPost = getRef(position).getKey();


                /** test for displaying no image */

                /**
                holder.usersName.setText(model.getFullName());
                holder.Date.setText(model.getDate());
                holder.Title.setText(model.getTitle());
                */

                /** test for displaying no image */

                //Picasso.get().load(model.getImage()).into(holder.Image);

                //holder.itemView

                holder.title_mypost.setText(model.getTitle());
                holder.description_mypost.setText(model.getDescription());
                holder.crimetype_mypost.setText(model.getCategory());
                holder.date_mypost.setText(model.getDate());

                holder.editPost_button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent GoToIndividualPost = new Intent(MyPosts.this, EditOccurrencePost.class);
                        GoToIndividualPost.putExtra("EditPostKey", SelectedMyPost);
                        startActivity(GoToIndividualPost);
                    }
                });
            }
        };

        myposts_recycler.setAdapter(adapter);

        adapter.startListening();
        //
    }





    //static class for "FirebseRecyclerAdapter" in method "DisplayAllPosts"
    public static class MyPostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView title_mypost, description_mypost, crimetype_mypost, date_mypost;

        CardView myposts_cardview;

        Button editPost_button, deletePost_button;

        //constructor
        public MyPostsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            title_mypost = itemView.findViewById(R.id.title_mypost);
            description_mypost = itemView.findViewById(R.id.description_mypost);
            crimetype_mypost = itemView.findViewById(R.id.crimetype_mypost);
            date_mypost = itemView.findViewById(R.id.date_mypost);

            myposts_cardview = itemView.findViewById(R.id.myposts_cardview);

            editPost_button = itemView.findViewById(R.id.editPost_button);
            deletePost_button = itemView.findViewById(R.id.deletePost_button);

        }

    }
}