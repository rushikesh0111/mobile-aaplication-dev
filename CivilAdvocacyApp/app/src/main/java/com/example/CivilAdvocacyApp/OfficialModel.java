package com.example.CivilAdvocacyApp;

import java.io.Serializable;
import java.util.ArrayList;

public class OfficialModel implements Serializable {
    private String name;
    private String title;
    private String address;
    private String party;
    private String phones;
    private String urls;
    private String emails;
    private String photoURL;

    private ArrayList<ChannelModel> channelModels;

    OfficialModel()
    {
        name = "";
        title = "";
        address = "";
        party = "";
        phones = "";
        urls = "";
        emails = "";
        photoURL = "";
        channelModels = new ArrayList<>();
    }

    //this method will return the name of the official
    public String getName()
    {
        return name;
    }

    //this method will return the title of the official
    String getTitle()
    {
        return title;
    }

    //this method will return the address of the official
    public String getAddress() {
        return address;
    }

    //this method will return the party of the official
    String getParty() {
        return party;
    }

    //this method will return the phone of the official
    public String getPhones()
    {
        return phones;
    }

    //this method will return the website URL of the official
    public String getUrls()
    {
        return urls;
    }

    //this method will return the EMAIL address of the official
    public String getEmails()
    {
        return emails;
    }

    //this method will return the URL of the photo of the official
    public String getPhotoURL()
    {
        return photoURL;
    }

    //this method will return the list of channelModels
    public ArrayList<ChannelModel> getChannels()
    {
        return channelModels;
    }

    //this method will set the name of the official
    public void setName(String name)
    {
        this.name = name;
    }

    //this method will set the title of the official
    void setTitle(String title)
    {
        this.title = title;
    }

    //this method will set the address of the official
    void setAddress(String address) {
        this.address = address;
    }

    //this method will set the party name of the official
    void setParty(String party)
    {
        this.party = party;
    }

    //this method will set the phone of the official
    void setPhones(String phones)
    {
        this.phones = phones;
    }

    //this method will set the website URL of the official
    void setUrls(String urls)
    {
        this.urls = urls;
    }

    //this method will set the Email address of the official
    void setEmails(String emails)
    {
        this.emails = emails;
    }

    //this method will set the URL of the photo of the official
    void setPhotoURL(String photoURL)
    {
        this.photoURL = photoURL;
    }

    //this method will set the chanelLists
    void setChannels(ArrayList<ChannelModel> channelModels)
    {
        this.channelModels = channelModels;
    }
}
