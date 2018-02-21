package com.breakcount.model;

/**
 * Created by gery on 15Sep2017.
 */

public abstract class Break {
    private String name;
    private int seconds;

    protected Break(int seconds, String name) {
        this.name = name;
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    protected void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
