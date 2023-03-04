package com.example.abc.newsaggregator.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.abc.newsaggregator.controllers.DateTimeUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ArticleModel implements Serializable, Parcelable {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private LocalDateTime publishedAt;


    //
    //
    //
    public ArticleModel(String author, String title, String description, String url,
                        String urlToImage, LocalDateTime publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

    //
    //
    //
    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

    //
    //
    //
    @Override
    public int describeContents() {
        return 0;
    }


    //
    //
    //
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(url);
        parcel.writeString(urlToImage);
        parcel.writeString(DateTimeUtils.formatDateTime(publishedAt));
    }

    //
    //
    //
    protected ArticleModel(Parcel in) {
        author = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
        urlToImage = in.readString();
    }

    //this method will get the name of author
    public String getAuthor() {
        return author;
    }

    //this method will get the title of an article
    public String getTitle() {
        return title;
    }

    //this method will get the description of an article
    public String getDescription() {
        return description;
    }

    //this method will get the URL or an article
    public String getUrl() {
        return url;
    }

    //this method will get the URL for an image related to that article
    public String getUrlToImage() {
        return urlToImage;
    }

    //this method will get the published time of an article
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
