package com.gkancheva.tripmanager.model.expense;

import java.util.Date;

public class Event extends Expense {
    protected Date mStartDate;
    protected Date mEndDate;
    protected String mStartPoint;
    protected String mEndPoint;
    protected String mProvider;
    protected String mConfirmation;

    public Event() {}

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public String getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(String mStartPoint) {
        this.mStartPoint = mStartPoint;
    }

    public String getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(String mEndPoint) {
        this.mEndPoint = mEndPoint;
    }

    public String getProvider() {
        return mProvider;
    }

    public void setProvider(String mProvider) {
        this.mProvider = mProvider;
    }

    public String getConfirmation() {
        return mConfirmation;
    }

    public void setConfirmation(String mConfirmation) {
        this.mConfirmation = mConfirmation;
    }
}
