package com.example.coraapp;


//this class retrieves information for the recycle adapter in MainActivity function "DisplayAllPosts"


public class Posts
{
    public String FullName, UID, date, title, description, category, ProfilePic;

    /** testing "" for image not present */
    public String  image = "";



    //default constructor
    public Posts()
    {

    }



    //constructors
    public Posts(String fullName, String UID, String date, String image, String title, String description, String category, String ProfilePic)
    {
        FullName = fullName;
        this.UID = UID;
        this.date = date;
        this.image = image;
        this.title = title;
        this.description = description;
        this.category = category;
        this.ProfilePic = ProfilePic;
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

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getProfile()
    {
        return ProfilePic;
    }

    public void setProfile(String category)
    {
        this.ProfilePic = ProfilePic;
    }
}
