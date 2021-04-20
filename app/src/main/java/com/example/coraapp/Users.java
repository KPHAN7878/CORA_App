package com.example.coraapp;


//this class retrieves information for the recycle adapter in AllUsersActivity function "DisplayAllUsers"


public class Users
{
    public String FullName;

    //default constructor
    public Users()
    {

    }


    //constructors
    public Users(String fullName)
    {
        FullName = fullName;
    }


    //getters and setters
    public String getFullName()
    {
        return FullName;
    }

    public void setFullName(String fullName)
    {
        this.FullName = fullName;
    }

}
