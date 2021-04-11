package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Threads extends AppCompatActivity
{
    private ImageView back_button, post_button;
    private TextView toolbar_text;

    /** get extra variable that determines what thread topic we are in*/
    private String testString;

    //recyclerview variables
    private RecyclerView ThreadsList;
    private FirebaseRecyclerAdapter adapter;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    //private DatabaseReference PostsRef;
    private Query ThreadsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);

        back_button = findViewById(R.id.back_button);
        post_button = findViewById(R.id.post_button);
        toolbar_text = findViewById(R.id.toolbar_text);


        /** get string extra from previous */
        testString = getIntent().getExtras().get("Topic").toString();

        toolbar_text.setText(testString);


        /** set up recyclerview */
        ThreadsList = (RecyclerView) findViewById(R.id.threads_post_list);
        ThreadsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ThreadsList.setLayoutManager(linearLayoutManager);

        back_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent backIntent = new Intent(Threads.this, ForumsHome.class);
                startActivity(backIntent);
            }
        });

        post_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent postIntent = new Intent(Threads.this, CreateThread.class);
                postIntent.putExtra("Topic", testString);
                startActivity(postIntent);
            }
        });

        DisplayAllThreads();


    }


    /** Function that displays all threads */
    private void DisplayAllThreads()
    {
        super.onStart();

        ThreadsRef = FirebaseDatabase.getInstance().getReference().child(testString + "Threads");

        FirebaseRecyclerOptions<ThreadsModel> options =
                new FirebaseRecyclerOptions.Builder<ThreadsModel>()


                        .setQuery(ThreadsRef, new SnapshotParser<ThreadsModel>()


                        {
                            @NonNull
                            @Override
                            public ThreadsModel parseSnapshot(@NonNull DataSnapshot snapshot)
                            {
                                return new ThreadsModel(snapshot.child("Username").getValue().toString(),
                                        snapshot.child("UID").getValue().toString(),
                                        snapshot.child("date").getValue().toString(),
                                        snapshot.child("image").getValue().toString(),
                                        snapshot.child("description").getValue().toString(),
                                        snapshot.child("title").getValue().toString());
                            }
                        })

                        //.setQuery(PostsRef, Posts.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ThreadsModel, Threads.ThreadsViewHolder>(options)
        {
            @Override
            public ThreadsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_thread_layout, parent, false);

                return new ThreadsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(Threads.ThreadsViewHolder holder, final int position, ThreadsModel model)
            {
                //get unique for each occurrence post
                final String OccurrenceKey = getRef(position).getKey();


                /** test for displaying no image */


                holder.Username.setText(model.getUsername());
                holder.Date.setText(model.getDate());
                holder.Title.setText(model.getTitle());
                holder.Description.setText(model.getDescription());

                /** test for displaying no image */

                //holder.itemView
            }
        };

        ThreadsList.setAdapter(adapter);

        adapter.startListening();
        //
    }

    //static class for "FirebseRecyclerAdapter" in method "DisplayAllPosts"
    public static class ThreadsViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username, Date, Title, Description;

        //constructor
        public ThreadsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            Username = itemView.findViewById(R.id.t_username);
            Date = itemView.findViewById(R.id.t_date);
            Title = itemView.findViewById(R.id.t_title);
            Description = itemView.findViewById(R.id.t_description);
        }

    }
}