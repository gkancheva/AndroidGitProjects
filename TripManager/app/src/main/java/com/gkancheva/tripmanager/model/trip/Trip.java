package com.gkancheva.tripmanager.model.trip;

import android.os.Parcel;
import android.os.Parcelable;

import com.gkancheva.tripmanager.model.expense.Expense;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip extends SugarRecord implements Parcelable {
    private Long mId;
    private Date mStartDate;
    private Date mEndDate;
    private String mName;
    private double mBudget;
    @Ignore
    private List<Expense> mExpenses;

    public Trip() {}

    public Trip(Date mStartDate, Date mEndDate, String mName, double mBudget) {
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
        this.mName = mName;
        this.mBudget = mBudget;
        this.mExpenses = new ArrayList<Expense>();
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

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getBudget() {
        return mBudget;
    }

    public void setBudget(double mBudget) {
        this.mBudget = mBudget;
    }

    public List<Expense> getExpenses() {
        return mExpenses;
    }

    public void setExpenses(List<Expense> mExpenses) {
        this.mExpenses = mExpenses;
    }

    protected Trip(Parcel in) {
        mId = in.readByte() == 0x00 ? null : in.readLong();
        long tmpMStartDate = in.readLong();
        mStartDate = tmpMStartDate != -1 ? new Date(tmpMStartDate) : null;
        long tmpMEndDate = in.readLong();
        mEndDate = tmpMEndDate != -1 ? new Date(tmpMEndDate) : null;
        mName = in.readString();
        mBudget = in.readDouble();
        if (in.readByte() == 0x01) {
            mExpenses = new ArrayList<Expense>();
            in.readList(mExpenses, Expense.class.getClassLoader());
        } else {
            mExpenses = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(mId);
        }
        dest.writeLong(mStartDate != null ? mStartDate.getTime() : -1L);
        dest.writeLong(mEndDate != null ? mEndDate.getTime() : -1L);
        dest.writeString(mName);
        dest.writeDouble(mBudget);
        if (mExpenses == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mExpenses);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
