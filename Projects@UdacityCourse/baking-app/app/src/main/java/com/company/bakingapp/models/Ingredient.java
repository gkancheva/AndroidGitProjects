package com.company.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Ingredient implements Parcelable {
    private long mId;
    private double mQuantity;
    private String mMeasure;
    private String mName;

    public Ingredient(double mQuantity, String mMeasure, String mName) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mName = mName;
    }

    public Ingredient(long id, double quantity, String measure, String name) {
        this(quantity, measure, name);
        this.mId = id;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public double getQuantity() {
        return this.mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }

    public String getMeasure() {
        return this.mMeasure;
    }

    public void setMeasure(String measure) {
        this.mMeasure = measure;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s - %.2f %s",
                this.getName(), this.getQuantity(), this.getMeasure());
    }

    protected Ingredient(Parcel in) {
        mId = in.readLong();
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeDouble(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}