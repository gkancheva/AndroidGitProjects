package com.gkancheva.tripmanager.model.expense;

import com.gkancheva.tripmanager.model.trip.Trip;
import com.orm.SugarRecord;

import java.util.Date;

public class Expense extends SugarRecord {
    protected ExpenseCategory mExpenseCategory;
    protected double mPrice;
    protected Date mDate;
    protected String mCurrency;
    protected PaymentMethod mPaymentMethod;
    protected String mReceiptPath;
    protected Trip mTripIdentifier;
    protected String mTitle;

    public Expense() {
    }

    public Expense(Trip mTripIdentifier, ExpenseCategory mExpenseCategory, String mTitle, double mPrice, Date mDate, String mCurrency, PaymentMethod mPaymentMethod) {
        this.mTripIdentifier = mTripIdentifier;
        this.mExpenseCategory = mExpenseCategory;
        this.mPrice = mPrice;
        this.mDate = mDate;
        this.mCurrency = mCurrency;
        this.mPaymentMethod = mPaymentMethod;
        this.mTitle = mTitle;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(PaymentMethod mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public String getReceiptPath() {
        return mReceiptPath;
    }

    public void setReceiptPath(String mReceipt) {
        this.mReceiptPath = mReceipt;
    }

    public Trip getTripIdentifier() {
        return mTripIdentifier;
    }

    public void setTripIdentifier(Trip mTripIdentifier) {
        this.mTripIdentifier = mTripIdentifier;
    }

    public ExpenseCategory getExpenseCategory() {
        return mExpenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory mExpenseCategory) {
        this.mExpenseCategory = mExpenseCategory;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
