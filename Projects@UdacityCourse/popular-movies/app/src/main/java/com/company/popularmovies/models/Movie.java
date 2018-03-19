package com.company.popularmovies.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private long mId;
    private String mOriginalName;
    private String mThumbnailPath;
    private Bitmap mThumbnail;
    private String mOverview;
    private double mRating;
    private Date mReleaseDate;

    public Movie(long id, String originalName, String thumbnailPath, Bitmap thumbnail, String overview, double rating, Date releaseDate) {
        this.mId = id;
        this.mOriginalName = originalName;
        this.mThumbnailPath = thumbnailPath;
        this.mThumbnail = thumbnail;
        this.mOverview = overview;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        this.mId = in.readLong();
        this.mOriginalName = in.readString();
        this.mThumbnailPath = in.readString();
        this.mThumbnail = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
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

    public String getThumbnailPath() {
        return this.mThumbnailPath;
    }

    public void setThumbnailPath(String thumbnail) {
        this.mThumbnailPath = thumbnail;
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

    public Bitmap getThumbnail() {
        return this.mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mOriginalName);
        parcel.writeString(this.mThumbnailPath);
        parcel.writeValue(this.mThumbnail);
        parcel.writeString(this.mOverview);
        parcel.writeDouble(this.mRating);
        parcel.writeLong(this.mReleaseDate != null ? this.mReleaseDate.getTime() : -1L);
    }
}