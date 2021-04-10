package com.example.coraapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapPlot extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    FirebaseDatabase mapDB;
    DatabaseReference mapRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_plot);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.crimeMap);
        mapFragment.getMapAsync(this);

        mapRef = mapDB.getInstance().getReference("Occurrence");
    }

    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;


        mapRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    CrimeLocations dp = ds.getValue(CrimeLocations.class);

                    //dp.getLatitude();
                    //dp.getLongitude();

                    LatLng location = new LatLng(Double.parseDouble(dp.latitude),Double.parseDouble(dp.longitude));

                    mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title("new marker"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });




        final Double[] latitude = {7.023};
        final Double[] longitude = {79.89};

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
}