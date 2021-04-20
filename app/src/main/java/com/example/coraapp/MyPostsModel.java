package com.example.coraapp;

public class MyPostsModel
{
    public String FullName, UID, date, title, description, category;

    /** testing "" for image not present */
    public String  image = "";



    //default constructor
    public MyPostsModel()
    {

    }



    //constructors
    public MyPostsModel(String fullName, String UID, String date, String image, String title, String description, String category)
    {
        FullName = fullName;
        this.UID = UID;
        this.date = date;
        this.image = image;
        this.title = title;
        this.description = description;
        this.category = category;
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

    public void setCategory(String description)
    {
        this.category = category;
    }
}
