package com.example.abc.newsaggregator.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LanguageModel implements Comparable<LanguageModel>, Parcelable {

    String code;
    String name;

    //
    public LanguageModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    //
    protected LanguageModel(Parcel in) {
        code = in.readString();
        name = in.readString();
    }

    //
    //
    @Override
    public int compareTo(LanguageModel languageModel) {
        if (this.getName() == null || languageModel.getName() == null) return 0;
        return this.getName().compareTo(languageModel.getName());
    }

    //
    //
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

    //this method will get the language code
    public String getCode() {
        return code;
    }

    //this method will get the language name
    public String getName() {
        return name;
    }

    //this method will set the language name
    public void setName(String name) {
        this.name = name;
    }

    public static final Creator<LanguageModel> CREATOR = new Creator<LanguageModel>() {
        @Override
        public LanguageModel createFromParcel(Parcel in) {
            return new LanguageModel(in);
        }

        @Override
        public LanguageModel[] newArray(int size) {
            return new LanguageModel[size];
        }
    };
}
