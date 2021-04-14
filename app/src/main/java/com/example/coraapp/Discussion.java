package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Discussion extends AppCompatActivity
{

    private String getThreadString;
    private String getTopicString;

    //firebase variables
    private DatabaseReference threadRef;
    private Query commentsRef;

    //recycleradapter variable
    private RecyclerView DiscussionList;
    private FirebaseRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        /** get string extra from previous */
        /**get specific thread key */
        getThreadString = getIntent().getExtras().get("ThreadKey").toString();
        /** get topic that thread is currently under */
        getTopicString = getIntent().getExtras().get("Topic").toString();


        /** set up recyclerview */
        DiscussionList = (RecyclerView) findViewById(R.id.discussion_recycler_id);
        DiscussionList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        DiscussionList.setLayoutManager(linearLayoutManager);

        //get instance of the specific thread
        //threadRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString);
        //get the comments
        //commentsRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString).child("comments");

        DisplayThreadInteraction();

    }



    private void DisplayThreadInteraction()
    {
        super.onStart();

        try
        {
            commentsRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString).child("comments");
        }
        catch (Exception e)
        {

        }


        //ThreadsRef = FirebaseDatabase.getInstance().getReference().child(testString + "Threads");

        FirebaseRecyclerOptions<DiscussionModel> options =
                new FirebaseRecyclerOptions.Builder<DiscussionModel>()


                        .setQuery(commentsRef, new SnapshotParser<DiscussionModel>()


                        {
                            @NonNull
                            @Override
                            public DiscussionModel parseSnapshot(@NonNull DataSnapshot snapshot)
                            {
                                return new DiscussionModel(snapshot.child("Username").getValue().toString(),
                                        snapshot.child("UID").getValue().toString(),
                                        snapshot.child("date").getValue().toString(),
                                        snapshot.child("image").getValue().toString(),
                                        snapshot.child("description").getValue().toString(),
                                        snapshot.child("title").getValue().toString(),
                                        snapshot.child("Uploader").getValue().toString());
                                //snapshot.child("Uploader").getValue().toString();
                            }
                        })

                        //.setQuery(PostsRef, Posts.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<DiscussionModel, Discussion.DiscussionsViewHolder>(options)
        {
            @Override
            public Discussion.DiscussionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.thread_uploader_template, parent, false);

                return new Discussion.DiscussionsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(Discussion.DiscussionsViewHolder holder, final int position, DiscussionModel model)
            {
                /** get unique key for each thread display */
                final String ThreadKey = getRef(position).getKey();

                /** test for displaying no image */


                holder.Username.setText(model.getUsername());
                holder.Date.setText(model.getDate());
                holder.Title.setText(model.getTitle());
                holder.Description.setText(model.getDescription());


                /** displaying image */
                //if an image is avilable
                if(model.getImage().toString().equals("null"))
                {
                    holder.Image.setVisibility(View.GONE);
                }
                else
                {
                    Picasso.get().load(model.getImage()).into(holder.Image);
                }


                /** test for displaying no image */

                //holder.itemView

                /** set on click listener on every thread display to gain access to discussion*/
                /***
                holder.thread_cardview_id.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent goToDiscussion = new Intent(Threads.this, Discussion.class);
                        //send unique thread key to next activity
                        goToDiscussion.putExtra("ThreadKey", ThreadKey);
                        goToDiscussion.putExtra("Topic", testString);
                        startActivity(goToDiscussion);
                    }
                });
                */
            }
        };

        DiscussionList.setAdapter(adapter);

        adapter.startListening();
    }


    //static class for "FirebseRecyclerAdapter" in method "DisplayAllPosts"
    public static class DiscussionsViewHolder extends RecyclerView.ViewHolder
    {
        TextView Username, Date, Title, Description;
        ImageView Image;
        CardView disc_cardview_id;

        //constructor
        public DiscussionsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            Username = itemView.findViewById(R.id.disc_username);
            Date = itemView.findViewById(R.id.disc_date);
            Title = itemView.findViewById(R.id.disc_title);
            Description = itemView.findViewById(R.id.disc_description);
            Image = itemView.findViewById(R.id.disc_image);

            disc_cardview_id = itemView.findViewById(R.id.disc_cardview_id);
        }

    }
}