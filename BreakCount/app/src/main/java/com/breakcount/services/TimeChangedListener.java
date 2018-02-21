package com.breakcount.services;

/**
 * Created by gery on 15Sep2017.
 */

public interface TimeChangedListener {
    void updateTime(int secondsLeft);
    void onFinish();
}
