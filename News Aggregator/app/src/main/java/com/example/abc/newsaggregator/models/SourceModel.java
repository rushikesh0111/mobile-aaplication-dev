package com.example.abc.newsaggregator.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SourceModel implements Serializable, Parcelable {

    private String id;
    private String name;
    private String category;
    private String language;
    private String country;

    //
    public SourceModel(String id, String name, String category, String language, String country) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.language = language;
        this.country = country;
    }

    //
    protected SourceModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        language = in.readString();
        country = in.readString();
    }

    // Parcelable implementation to save instance state
    @Override
    public int describeContents() {
        return 0;
    }

    //
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
        parcel.writeString(language);
        parcel.writeString(country);
    }

    //this method will get the id of an article
    public String getId() {
        return id;
    }

    //this method will set the id of an article
    public void setId(String id) {
        this.id = id;
    }

    //this method will get the name of an article
    public String getName() {
        return name;
    }

    //this method will set the name of an article
    public void setName(String name) {
        this.name = name;
    }

    //this method will get the category of an article
    public String getCategory() {
        return category;
    }

    //this method will set the category of an article
    public void setCategory(String category) {
        this.category = category;
    }

    //this method will get the language of an article
    public String getLanguage() {
        return language;
    }

    //this method will set the language of an article
    public void setLanguage(String language) {
        this.language = language;
    }

    //this method will get the country associated with an article
    public String getCountry() {
        return country;
    }

    //this method will set the coutry of an article
    public void setCountry(String country) {
        this.country = country;
    }

    //
    //
    public static final Creator<SourceModel> CREATOR = new Creator<SourceModel>() {
        @Override
        public SourceModel createFromParcel(Parcel in) {
            return new SourceModel(in);
        }

        @Override
        public SourceModel[] newArray(int size) {
            return new SourceModel[size];
        }
    };
}
