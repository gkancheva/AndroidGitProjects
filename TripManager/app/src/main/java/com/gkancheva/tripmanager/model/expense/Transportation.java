package com.gkancheva.tripmanager.model.expense;

import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.Date;

public class Transportation extends Event {

    public Transportation() {
    }

    public Transportation(ExpenseCategory mExpenseCategory, Trip mTrip, String mDeparturePoint, String mArrivalPoint,
                          Date mDateTimeDeparture, Date mDateTimeArrival, String mCompanyProvider,
                          String mConfirmationNumber, String mTitle, double mPrice, Date mDateExp, String mCurrency, PaymentMethod mPaymentMethod) {
        super();
        this.mExpenseCategory = mExpenseCategory;
        this.setTripIdentifier(mTrip);
        this.mStartPoint = mDeparturePoint;
        this.mEndPoint = mArrivalPoint;
        this.mStartDate = mDateTimeDeparture;
        this.mEndDate = mDateTimeArrival;
        this.mProvider = mCompanyProvider;
        this.mConfirmation = mConfirmationNumber;
        this.mPrice = mPrice;
        this.mCurrency = mCurrency;
        this.mDate = mDateExp;
        this.mPaymentMethod = mPaymentMethod;
        this.mTitle = mTitle;
    }
}
