package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class Discussion extends AppCompatActivity
{

    private String getThreadString;
    private String getTopicString;

    //firebase variables
    private DatabaseReference threadRef;
    private Query commentsRef;
    private FirebaseAuth mAuth;
    private StorageReference ImageRef;
    private DatabaseReference UserRef, ThreadsRef;

    //recycleradapter variable
    private RecyclerView DiscussionList;
    private FirebaseRecyclerAdapter adapter;

    //commenting items
    EditText comment_edittext_id;
    ImageButton uploadpic_id;
    ImageView send_comment_id;

    /** variables to store comment content to upload to firebase */
    private String commentDescription;
    private Uri imageUri = null;
    private static final int GALLERY_PICK = 1;


    //time variables to create unique image name
    private String saveCurrentDate, saveCurrentTime, postRandomID;
    String storageURL;
    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        /** get current user reference */
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        /** get string extra from previous */
        /**get specific thread key */
        getThreadString = getIntent().getExtras().get("ThreadKey").toString();
        /** get topic that thread is currently under */
        getTopicString = getIntent().getExtras().get("Topic").toString();


        //references to user and occurrence in database
        //reference to image in storage
        ImageRef = FirebaseStorage.getInstance().getReference();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ThreadsRef = FirebaseDatabase.getInstance().getReference().child(getTopicString);
        //ThreadsRef = FirebaseDatabase.getInstance().getReference().child(getTopicString + "Threads");


        //initialize commenting items
        comment_edittext_id = findViewById(R.id.comment_edittext_id);
        uploadpic_id = findViewById(R.id.uploadpic_id);
        send_comment_id = findViewById(R.id.send_comment_id);



        /** set up recyclerview */
        DiscussionList = (RecyclerView) findViewById(R.id.discussion_recycler_id);
        DiscussionList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        DiscussionList.setLayoutManager(linearLayoutManager);

        //get instance of the specific thread
        //threadRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString);
        //get the comments
        //commentsRef = FirebaseDatabase.getInstance().getReference().child(getTopicString).child(getThreadString).child("comments");

        DisplayThreadInteraction();

        //set on click listener on the upload image icon
        uploadpic_id.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });


        //set on click listener on the post button for comments
        send_comment_id.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InitiazlizeComment();
            }
        });

    }


    //method to open gallery
    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }


    //method that gets image from gallery and displays on image button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode==RESULT_OK)
        {
            imageUri = data.getData();
            //uploadpic_id.setImageURI(imageUri);
        }
    }


    //method to initialize comments
    private void InitiazlizeComment()
    {
        commentDescription = comment_edittext_id.getText().toString();

        if(TextUtils.isEmpty(commentDescription))
        {
            Toast.makeText(this, "Please Enter Comment", Toast.LENGTH_SHORT).show();
        }
        //if the comment does not have an image
        else if(imageUri == null)
        {
            CommentWithNoImage();
        }
        //if the comment does have an image
        else
        {
            CommentWithImage();
        }

    }


    //function to send comment data with image to firebase
    private void CommentWithImage()
    {
        Calendar obtainDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;

        StorageReference filePath = ImageRef.child("occurrence image").child(imageUri.getLastPathSegment() + postRandomID + ".jpg");

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
                        storageURL = uri.toString();


                        //this part is retrieving user's name from database to link with their post
                        UserRef.child(userID).addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                if(snapshot.exists())
                                {
                                    String fullname = snapshot.child("FullName").getValue().toString();
                                    String username = snapshot.child("Username").getValue().toString();


                                    //create new node in database for occurrence and store information
                                    // -store userID
                                    // - store date
                                    // - sotre title
                                    // - store storage url for image
                                    // - store full name
                                    HashMap occurrenceMap = new HashMap();
                                    occurrenceMap.put("UID", userID);
                                    occurrenceMap.put("date", saveCurrentDate);
                                    occurrenceMap.put("title", "null");
                                    occurrenceMap.put("description", commentDescription);
                                    occurrenceMap.put("image", storageURL);
                                    occurrenceMap.put("FullName", fullname);
                                    occurrenceMap.put("Username", username);
                                    occurrenceMap.put("Uploader", "no");

                                    /**
                                    //add new threads to firebase under respective thread category node and assign unique ID for each post
                                    ThreadsRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Intent GoToThread = new Intent(Discussion.this, Threads.class);
                                                startActivity(GoToThread);
                                                Toast.makeText(Discussion.this, "Thank you", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(Discussion.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    */

                                    /** Add a copy of the thread data under subnode "comments" to use single recycleradapter when displaying in discussion later */
                                    ThreadsRef.child(getThreadString).child("comments").child(userID + postRandomID + "-comment").updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error)
                            {

                            }
                        });




                    }
                });


                //ReportInfoToDatabase();
            }
        });
    }


    //function to send comment data with no image to firebase
    private void CommentWithNoImage()
    {
        /** get time to store time and to use in unique id creation */
        Calendar obtainDate = Calendar.getInstance();
        android.icu.text.SimpleDateFormat currentDate = new android.icu.text.SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(obtainDate.getTime());

        Calendar obtainTime = Calendar.getInstance();
        android.icu.text.SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(obtainTime.getTime());

        postRandomID = saveCurrentDate + saveCurrentTime;

        //this part is retrieving user's name from database to link with their post
        UserRef.child(userID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    //get user's full name
                    String fullname = snapshot.child("FullName").getValue().toString();
                    String username = snapshot.child("Username").getValue().toString();
                    storageURL = "null";


                    //create new node in database for occurrence and store information
                    // -store userID
                    // - store date
                    // - sotre title
                    // - store storage url for image
                    // - store full name
                    HashMap occurrenceMap = new HashMap();
                    occurrenceMap.put("UID", userID);
                    occurrenceMap.put("date", saveCurrentDate);
                    occurrenceMap.put("title", "null");
                    occurrenceMap.put("description", commentDescription);
                    occurrenceMap.put("image", storageURL);
                    occurrenceMap.put("FullName", fullname);
                    occurrenceMap.put("Username", username);
                    occurrenceMap.put("Uploader", "no");

                    /**
                     *
                    //add new occurrence reports to firebase under "Occurrence" node and assign unique ID for each post
                    ThreadsRef.child(userID + postRandomID).updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Intent GoToThread = new Intent(Discussion.this, Threads.class);
                                startActivity(GoToThread);
                                Toast.makeText(Discussion.this, "Thank you", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Discussion.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    */

                    /** Add a copy of the thread data under subnode "comments" to use single recycleradapter when displaying in discussion later */
                    ThreadsRef.child(getThreadString).child("comments").child(userID + postRandomID + "comment").updateChildren(occurrenceMap).addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }


    /** main display method for activity */
    //method that uses recyclerviewadapter to display all comments under thread discussion
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
                holder.Description.setText(model.getDescription());

                if(model.getTitle().toString().equals("null"))
                {
                    holder.Title.setVisibility(View.GONE);
                }
                else
                {
                    holder.Title.setText(model.getTitle());
                }

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