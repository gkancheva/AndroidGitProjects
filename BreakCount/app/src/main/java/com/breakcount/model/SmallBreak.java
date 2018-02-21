package com.breakcount.model;

/**
 * Created by gery on 15Sep2017.
 */

public class SmallBreak extends Break {
    private static final String NAME = "Small break";
    private static final int SECONDS = 30;

    public SmallBreak() {
        super(SECONDS, NAME);
    }
}
