package com.company.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private long mId;
    private String mOriginalName;
    private  String mThumbnail;
    private String mOverview;
    private double mRating;
    private Date mReleaseDate;

    public Movie(long id, String originalName, String thumbnail, String overview, double rating, Date releaseDate) {
        this.mId = id;
        this.mOriginalName = originalName;
        this.mThumbnail = thumbnail;
        this.mOverview = overview;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        this.mId = in.readLong();
        this.mOriginalName = in.readString();
        this.mThumbnail = in.readString();
        this.mOverview = in.readString();
        this.mRating = in.readDouble();
        long releaseDateInMilliSec = in.readLong();
        this.mReleaseDate = releaseDateInMilliSec != -1 ? new Date(releaseDateInMilliSec) : null;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getOriginalName() {
        return this.mOriginalName;
    }

    public void setOriginalName(String originalName) {
        this.mOriginalName = originalName;
    }

    public String getThumbnail() {
        return this.mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public String getOverview() {
        return this.mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public double getRating() {
        return this.mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public Date getReleaseDate() {
        return this.mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mOriginalName);
        parcel.writeString(this.mThumbnail);
        parcel.writeString(this.mOverview);
        parcel.writeDouble(this.mRating);
        parcel.writeLong(this.mReleaseDate != null ? this.mReleaseDate.getTime() : -1L);
    }
}