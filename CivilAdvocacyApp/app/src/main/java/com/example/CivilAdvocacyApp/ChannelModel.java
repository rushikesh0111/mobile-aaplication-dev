package com.example.CivilAdvocacyApp;

import java.io.Serializable;

public class ChannelModel implements Serializable
{
    private String type;
    private String id;

    public ChannelModel(String type, String id)
    {
        this.type = type;
        this.id = id;
    }

    //this method will return the type
    public String getType()
    {
        return type;
    }

    //this method will return the id
    public String getId()
    {
        return id;
    }
}
