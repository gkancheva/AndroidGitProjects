package com.gkancheva.tripmanager.model.user;

import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long mId;
    private String mFirstName;
    private String mLastName;
    private String mPassword;
    private String mUsername;
    private List<Trip> mTrips;

    public User() {}

    public User(String mFirstName, String mLastName, String mPassword, String mUsername) {
        //this.mId = mId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPassword = mPassword;
        this.mUsername = mUsername;
        this.mTrips = new ArrayList<>();
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public List<Trip> getTrips() {
        return mTrips;
    }

    public void setTrips(List<Trip> mTrips) {
        this.mTrips = mTrips;
    }
}
