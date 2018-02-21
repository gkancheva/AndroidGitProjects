package com.notesapp.utils;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by gery on 29Aug2017.
 */

public class ProgrBar {
    private ProgressBar progressBar;

    public ProgrBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void start() {
        progressBar.setVisibility(View.VISIBLE);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

}
