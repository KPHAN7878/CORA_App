package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolBar;

    //recyclerview variables
    private RecyclerView postList;
    private FirebaseRecyclerAdapter adapter;


    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    //private DatabaseReference PostsRef;
    private Query PostsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        //test

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //get reference to "Occurrence" node for recycleview method


        //PostsRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");
        //PostsRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");

        //adding the toolbar
        mToolBar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Home");


        /** nav bar stuff */
        drawerLayout = findViewById(R.id.draw_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);

        //test
        /**View navView = navigationView.inflateHeaderView(R.layout.navigation_header);*/


        /*
        //recyclerview for displaying posts
        postList = findViewById(R.id.all_users_post_list);
        postList.setLayoutManager(new LinearLayoutManager(this));
        */
        
        postList = (RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        /*
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //arranging posts from newest to oldest
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        */

        //
        //access navigation_menu.xml in menu folder when clicked
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                GoToNavMenu(item);
                return false;
            }
        });


        DisplayAllPosts();


    }
    //method that displays all posts
    //using firebse recycle adapter
    private void DisplayAllPosts()
    {
        super.onStart();

        PostsRef = FirebaseDatabase.getInstance().getReference().child("Occurrence");

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()


                    .setQuery(PostsRef, new SnapshotParser<Posts>()


                    {
                        @NonNull
                        @Override
                        public Posts parseSnapshot(@NonNull DataSnapshot snapshot)
                        {
                            return new Posts(snapshot.child("FullName").getValue().toString(),
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

        adapter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options)
        {
            @Override
            public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.occurrence_post_layout, parent, false);

                return new PostsViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(PostsViewHolder holder, final int position, Posts model)
            {
                //get unique for each occurrence post
                final String OccurrenceKey = getRef(position).getKey();


                /** test for displaying no image */


                holder.usersName.setText(model.getFullName());
                holder.Date.setText(model.getDate());
                holder.Title.setText(model.getTitle());
                holder.Description.setText(model.getDescription());
                holder.Category.setText(model.getCategory());

                /** test for displaying no image */

                //Picasso.get().load(model.getImage()).into(holder.Image);

                if(model.getImage().toString().equals("null"))
                {
                    holder.Image.setVisibility(View.GONE);
                }
                else
                {
                    Picasso.get().load(model.getImage()).into(holder.Image);
                }

                //holder.itemView
            }
        };

        postList.setAdapter(adapter);

        adapter.startListening();
        //
    }





    //static class for "FirebseRecyclerAdapter" in method "DisplayAllPosts"
    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView usersName, Date, Title, Description, Category;
        ImageView Image;

        //constructor
        public PostsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            usersName = itemView.findViewById(R.id.post_user_name);
            Date = itemView.findViewById(R.id.post_date);
            Title = itemView.findViewById(R.id.post_title);
            Image = itemView.findViewById(R.id.post_image);
            Description = itemView.findViewById(R.id.home_description);
            Category = itemView.findViewById(R.id.home_type);
        }

    }





    //action bar hamburger toggle
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    //method that redirects to navigation bar
    private void GoToNavMenu(MenuItem item)
    {

        //actions that happen when user clicks on certain navigation buttons
        switch(item.getItemId())
        {
            case R.id.nav_home:
                Toast.makeText(this, "Home button clicked", Toast.LENGTH_SHORT).show();
                Intent addCategory = new Intent(MainActivity.this, AddCategory.class);
                startActivity(addCategory);
                break;
            case R.id.nav_create:
                SendUserToPostActivity();
                break;
            case R.id.nav_account:
                Toast.makeText(this, "Account button clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_posts:
                //Toast.makeText(this, "Post button clicked", Toast.LENGTH_SHORT).show();
                Intent GoToMyPosts = new Intent(MainActivity.this, MyPosts.class);
                startActivity(GoToMyPosts);
                break;
            /** added */
            case R.id.nav_map:
                SendUserToMapActivity();
                break;
            case R.id.nav_forums:
                Intent ForumsIntent = new Intent(MainActivity.this, ForumsHome.class);
                startActivity(ForumsIntent);
                break;
            /** added */
            case R.id.nav_charts:
                SendUserToChartsActivity();
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
            case R.id.nav_admin:
                SendUserToAdminActivity();
                break;
            case R.id.nine_one_one:
                CallNineOneOne();
                break;
        }
    }

    /** add later */
    private void SendUserToChartsActivity()
    {
    }


    /** add later */
    private void SendUserToMapActivity()
    {
        Intent viewMapIntent = new Intent(MainActivity.this, MapPlot.class);
        startActivity(viewMapIntent);
    }


    //method that goes to post activity
    private void SendUserToPostActivity()
    {
        Intent newReportIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(newReportIntent);
    }

    //Admin stuff
    private void SendUserToAdminActivity()
    {
        Intent adminIntent = new Intent(MainActivity.this,AdminActivity.class);
        startActivity(adminIntent);
    }

    private void CallNineOneOne()
    {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+911));//change the number
        startActivity(callIntent);
    }

    /* //------------------------------------------------------------------------------------------

    //when app is opened make sure that the user is logged in
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }

        //check if user exists in database
        else
        {
            CheckUserExistence();
        }
    }

    */ // ----------------------------------------------------------------------------------------


    /* //------------------------------------------------------------------------------------------

    //method that checks if user is in database
    private void CheckUserExistence()
    {
        final String currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //MIGHT NEED TO EDIT THIS LATER - TUTORIAL 12 BUG

                //if user is authenticated (email and password) but is not in database yet (name, username, profile, etc)
                //if this is the case send user to setup page
                if(!snapshot.hasChild(currentUserID))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    */ // ----------------------------------------------------------------------------------------

    /* //------------------------------------------------------------------------------------------

    //method that sends user to setup activity
    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    */ // ----------------------------------------------------------------------------------------


    //login validation method
    //if user is not logged in then redirect to login screen
    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}