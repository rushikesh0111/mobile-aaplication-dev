package com.example.abc.newsaggregator.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryModel implements Comparable<CountryModel>, Parcelable {

    String code;
    String name;

    //
    public CountryModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    //
    protected CountryModel(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    //
    //
    @Override
    public int compareTo(CountryModel countryModel) {
        if (countryModel != null && this.getName() != null && countryModel.getName() != null) {
            return this.getName().compareTo(countryModel.getName());
        }
        return 0;
    }

    //
    // Parcelable implementation to save instance state
    @Override
    public int describeContents() {
        return 0;
    }

    //
    //
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(name);
    }

    //
    //
    public static final Creator<CountryModel> CREATOR = new Creator<CountryModel>() {
        @Override
        public CountryModel createFromParcel(Parcel in) {
            return new CountryModel(in);
        }

        @Override
        public CountryModel[] newArray(int size) {
            return new CountryModel[size];
        }
    };

    //this method will get the country code
    public String getCode() {
        return code;
    }

    //this method will set the country code
    public void setCode(String code) {
        this.code = code;
    }

    //this method will get the country name
    public String getName() {
        return name;
    }

    //this method will set the country name
    public void setName(String name) {
        this.name = name;
    }

}
