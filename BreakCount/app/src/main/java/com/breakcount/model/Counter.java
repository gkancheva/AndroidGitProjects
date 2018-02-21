package com.breakcount.model;

import android.os.CountDownTimer;

import com.breakcount.services.TimeChangedListener;

/**
 * Created by gery on 15Sep2017.
 */

public class Counter {
    private CountDownTimer timer;
    private boolean active;
    private int secondsLeft;
    private TimeChangedListener listener;

    public Counter(TimeChangedListener listener) {
        this.listener = listener;
    }

    public void setIsActive(boolean counterIsActive) {
        this.active = counterIsActive;
    }

    public boolean isActive() {
        return active;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void startTimer(int seconds) {
        int timerValue = seconds * 1000 + 100;
        timer = new CountDownTimer(timerValue, 1000) {
            @Override
            public void onTick(long l) {
                secondsLeft = (int) l / 1000;
                listener.updateTime(secondsLeft);
            }
            @Override
            public void onFinish() {
                listener.onFinish();
            }
        }.start();
    }

    public void cancelTimer() {
        timer.cancel();
    }
}
