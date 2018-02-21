package com.gkancheva.tripmanager.model.expense;

import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.Date;

public class RentACar extends Event {
    private String mType;

    public RentACar() {
    }

    /**
     * Constructor to be invoked for pick-up
     */
    public RentACar(ExpenseCategory mExpCat, Trip mTrip, String mCompanyProvider, String mConfirmation,
                    String mPickUpLocation, Date mPickUpDateTime, String mTitle, double mPrice, Date mDateExpense, String mCurrency,
                    PaymentMethod mPaymentMethod, String mType) {
        super();
        this.mExpenseCategory = mExpCat;
        this.mTripIdentifier = mTrip;
        this.mProvider = mCompanyProvider;
        this.mConfirmation = mConfirmation;
        this.mStartPoint = mPickUpLocation;
        this.mStartDate = mPickUpDateTime;
        this.mPrice = mPrice;
        this.mDate = mDateExpense;
        this.mCurrency = mCurrency;
        this.mPaymentMethod = mPaymentMethod;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    /**
     * Constructor to be invoked for drop-off
     */
    public RentACar(ExpenseCategory mExpCat, Trip mTrip, String mCompanyProvider, String mConfirmation, String mTitle,
                    String mDropOffLocation, Date mDroOffDateTime, String mType) {
        super();
        this.mExpenseCategory = mExpCat;
        this.mTripIdentifier = mTrip;
        this.mProvider = mCompanyProvider;
        this.mConfirmation = mConfirmation;
        this.mEndPoint = mDropOffLocation;
        this.mStartDate = mDroOffDateTime;
        this.mType = mType;
        this.mTitle = mTitle;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
