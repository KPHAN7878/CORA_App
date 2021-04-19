package com.example.coraapp;

public class ThreadsModel
{
    public String Username, UID, date, description, title;
    public String image = "";


    public ThreadsModel()
    {

    }

    public ThreadsModel(String username, String UID, String date, String image, String description, String title)
    {
        Username = username;
        this.UID = UID;
        this.date = date;
        this.image = image;
        this.description = description;
        this.title = title;
    }

    public String getUsername()
    {
        return Username;
    }
    public void setUsername(String username)
    {
        this.Username = username;
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


    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
}
