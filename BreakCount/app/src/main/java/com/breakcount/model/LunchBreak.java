package com.breakcount.model;

/**
 * Created by gery on 15Sep2017.
 */

public class LunchBreak extends Break {
    private static final String NAME = "Lunch break";
    private static final int SECONDS = 3600;

    public LunchBreak() {
        super(SECONDS, NAME);
    }

    @Override
    public String toString() {
        return super.getName();
    }
}
