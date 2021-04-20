package com.example.coraapp;

public class CrimeLocations
{
    public String latitude, longitude, category;

    public CrimeLocations()
    {

    }

    public CrimeLocations(String latitude, String longitude, String category)
    {
        this.latitude=latitude;
        this.longitude=longitude;
        this.category = category;
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
