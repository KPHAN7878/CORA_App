package com.example.coraapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

//test

public class GetLocation extends AppCompatActivity implements View.OnClickListener
{

    private TextView txtLatLong;
    private TextView txtAddress;
    private static final int ADDRESS_PICKER_REQUEST = 1020;

    private Button submit_location_btn;

    /** variables to store coordinate */
    private String lat;
    private String lng;
    private String zipcode;
    private String city;

    //String api_key = "AIzaSyCxq53zuF4EKyVIi6XkBQIJJ62GOUf4ACU";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        //this part ????????
        MapUtility.apiKey = getResources().getString(R.string.your_api_key);
        findViewById(R.id.btnAddressPicker).setOnClickListener(this);

        //button initializer
        //submit_location_btn = findViewById(R.id.submit_location_btn);

        txtLatLong = findViewById(R.id.txtLatLong);
        txtAddress = findViewById(R.id.txtAddress);




        /*
        submit_location_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent gotLocIntent = new Intent(GetLocation.this, PostActivity.class);

                gotLocIntent.putExtra("lat", lat);
                gotLocIntent.putExtra("lng", lng);

                startActivity(gotLocIntent);
                finish();

            }
        });
        */

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddressPicker:
                Intent intent = new Intent(GetLocation.this, LocationPickerActivity.class);
                startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress =data.getBundleExtra("fullAddress");


                    /* data in completeAddress bundle
                    "fulladdress"
                    "city"
                    "state"
                    "postalcode"
                    "country"
                    "addressline1"
                    "addressline2"
                     */

                    /** convert long and lat to string */
                    lat = String.valueOf(currentLatitude);
                    lng = String.valueOf(currentLongitude);



                    txtAddress.setText(new StringBuilder().append("addressline2: ").append
                            (completeAddress.getString("addressline2")).append("\ncity: ").append
                            (completeAddress.getString("city")).append("\npostalcode: ").append
                            (completeAddress.getString("postalcode")).append("\nstate: ").append
                            (completeAddress.getString("state")).toString());

                    txtLatLong.setText(new StringBuilder().append("Lat:").append(currentLatitude).append
                            ("  Long:").append(currentLongitude).toString());


                    //get zipcode
                    zipcode = String.valueOf(new StringBuilder().append(completeAddress.getString("postalcode")));

                    //get city
                    city = String.valueOf(new StringBuilder().append(completeAddress.getString("city")));

                    Intent gotLocIntent = new Intent(GetLocation.this, PostActivity.class);
                    gotLocIntent.putExtra("lat", lat);
                    gotLocIntent.putExtra("lng", lng);
                    gotLocIntent.putExtra("zip", zipcode);
                    gotLocIntent.putExtra("city", city);

                    startActivity(gotLocIntent);



                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}