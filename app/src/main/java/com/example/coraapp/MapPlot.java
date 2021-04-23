package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapPlot extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    FirebaseDatabase mapDB;
    DatabaseReference mapRef;

    private String TypeFilterFlag;
    private String ZipFilter;

    private ArrayList<String> CrimeType = new ArrayList<String>();

    private ImageView filter_button;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_plot);

        /** not sure if correct */
        filter_button = findViewById(R.id.post_button);

        geocoder = new Geocoder(this);


        /** if the filter button is clicked in the toolbar */
        filter_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Bundle extra = new Bundle();
                extra.putSerializable("crimefilters", CrimeType);
                Intent GoToMapFilter = new Intent(MapPlot.this, MapFilter.class);
                GoToMapFilter.putExtra("extra", extra);
                startActivity(GoToMapFilter);
            }
        });


        /** map stuff */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.crimeMap);
        mapFragment.getMapAsync(this);

        mapRef = mapDB.getInstance().getReference("Occurrence");
    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        /** get array list from MapFilter activity */
        try
        {
            Bundle extra = getIntent().getBundleExtra("extra");
            CrimeType = (ArrayList<String>) extra.getSerializable("filterSetted");
        }
        catch (Exception e)
        {

        }



            mapRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        CrimeLocations coord = ds.getValue(CrimeLocations.class);

                        //dp.getLatitude();
                        //dp.getLongitude();

                        /**
                         LatLng location = new LatLng(Double.parseDouble(coord.latitude),Double.parseDouble(coord.longitude));
                         */

                        try
                        {

                            String lat = coord.latitude;
                            String lng = coord.longitude;

                            String CrimeCategory = coord.category;

                            double dlat = Double.parseDouble(lat);
                            double dlang = Double.parseDouble(lng);


                            /** if statements are for filter feature */
                            if(CrimeType.isEmpty())
                            {
                                if(CrimeCategory.equals("Theft"))
                                {
                                    /** customize markers */
                                    BitmapDrawable Theftbitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.theft2);
                                    Bitmap bitmap = Theftbitmap.getBitmap();
                                    Bitmap marker = Bitmap.createScaledBitmap(bitmap, 100, 100,false);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("Theft")).setIcon(BitmapDescriptorFactory.fromBitmap(marker));
                                }
                                else if(CrimeCategory.equals("Burglary"))
                                {
                                    /** customize markers */
                                    BitmapDrawable Theftbitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.theft);
                                    Bitmap bitmap = Theftbitmap.getBitmap();
                                    Bitmap marker = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("Burglary")).setIcon(BitmapDescriptorFactory.fromBitmap(marker));;
                                }
                                else if(CrimeCategory.equals("Assault"))
                                {
                                    /** customize markers */
                                    BitmapDrawable Assaultbitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.attack);
                                    Bitmap bitmap = Assaultbitmap.getBitmap();
                                    Bitmap marker = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("Assault")).setIcon(BitmapDescriptorFactory.fromBitmap(marker));;
                                }
                                else if(CrimeCategory.equals("Murder"))
                                {
                                    /** customize markers */
                                    BitmapDrawable Theftbitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.murder);
                                    Bitmap bitmap = Theftbitmap.getBitmap();
                                    Bitmap marker = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("Murder")).setIcon(BitmapDescriptorFactory.fromBitmap(marker));;
                                }
                                else if(CrimeCategory.equals("Other"))
                                {
                                    /** customize markers */
                                    BitmapDrawable Theftbitmap = (BitmapDrawable) getResources().getDrawable(R.drawable.suspicious);
                                    Bitmap bitmap = Theftbitmap.getBitmap();
                                    Bitmap marker = Bitmap.createScaledBitmap(bitmap, 100, 100,false);

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("Other")).setIcon(BitmapDescriptorFactory.fromBitmap(marker));;
                                }
                            }

                            //fix later for filter
                            /**
                            else
                            {
                                CrimeType.contains(CrimeCategory);
                                {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dlat, dlang))
                                            .title("new marker"));
                                }
                            }
                            */

                            Toast.makeText(MapPlot.this, "lat: " + lat, Toast.LENGTH_SHORT).show();

                        } catch (Exception e)
                        {
                            Toast.makeText(MapPlot.this, "could not get coordinates", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         mMap.addMarker(new MarkerOptions()
                         .position(location)
                         .title("new marker"));

                         */

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });


        //set map focus on user's address
        String userID;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String address = snapshot.child("Address").getValue().toString();

                    //Geocoder geocoder = new Geocoder(this);
                    List<Address> addy;
                    LatLng coordinate = null;


                    try
                    {
                        addy = geocoder.getFromLocationName(address, 5);

                        Address location = addy.get(0);
                        coordinate = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.addMarker(new MarkerOptions()
                                .position(coordinate)
                                .title("Home"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12f));

                    }
                    catch (IOException ex)
                    {
                        Log.e("LocateMe", "Could not get geocoder data", ex);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }
}