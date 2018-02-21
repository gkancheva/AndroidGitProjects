package com.gkancheva.tripmanager.model.expense;

import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.Date;

public class Accommodation extends Event {
    protected String mType;

    public Accommodation() {
    }

    /**
     * Constructor to be invoked for checkIn
     */
    public Accommodation(ExpenseCategory mExpCategory, Trip mTrip, String mHotelName, String mHotelAddress, String mConfirmation,
                         Date mCheckIn, String mTitle, double mPrice, Date mDateExpense, String mCurrency, PaymentMethod mPaymentMethod, String mType) {
        super();
        this.mExpenseCategory = mExpCategory;
        this.mTripIdentifier = mTrip;
        this.mProvider = mHotelName;
        this.mStartPoint = mHotelAddress;
        this.mConfirmation = mConfirmation;
        this.mStartDate = mCheckIn;
        this.mPrice = mPrice;
        this.mDate = mDateExpense;
        this.mCurrency = mCurrency;
        this.mPaymentMethod = mPaymentMethod;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    /**
     * Constructor to be invoked for checkOut
     */
    public Accommodation(ExpenseCategory mExpCategory, Trip mTrip, String mHotelName, String mHotelAddress, String mConfirmation, String mTitle,
                         Date mCheckOut, String mType) {
        super();
        this.mExpenseCategory = mExpCategory;
        this.mTripIdentifier = mTrip;
        this.mProvider = mHotelName;
        this.mEndPoint = mHotelAddress;
        this.mConfirmation = mConfirmation;
        this.mStartDate = mCheckOut;
        this.mType = mType;
        this.mTitle = mTitle;
    }

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

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
