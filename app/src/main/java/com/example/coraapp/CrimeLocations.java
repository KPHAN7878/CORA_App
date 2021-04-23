package com.example.coraapp;

public class CrimeLocations
{
    public String latitude, longitude, category, zipcode;

    public CrimeLocations()
    {

    }

    public CrimeLocations(String latitude, String longitude, String category, String zipcode)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.category = category;
        this.zipcode = zipcode;
    }

    /*
    public Double getLatitude()
    {
        return latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }
    */


}
