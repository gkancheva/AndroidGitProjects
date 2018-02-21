package com.breakcount.model;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.breakcount.R;

import java.util.Locale;

/**
 * Created by gery on 15Sep2017.
 */

public class TimerView {
    private View view;
    private TextView txtSmallBreakLeft;
    private TextView txtLunchBreakLeft;
    private Button btnController;

    public TimerView(View view) {
        this.view = view;
        this.setViews();
    }

    private void setViews() {
        txtSmallBreakLeft = this.view.findViewById(R.id.txt_small_break);
        txtLunchBreakLeft =  this.view.findViewById(R.id.txt_lunch_break);
        btnController = view.findViewById(R.id.btn_start_count);
        btnController.setText("START");
        btnController.setEnabled(false);
    }

    public void setOnClickListeners(View.OnClickListener listener) {
        txtLunchBreakLeft.setOnClickListener(listener);
        txtSmallBreakLeft.setOnClickListener(listener);
        btnController.setOnClickListener(listener);
    }

    public void resetTimer() {
        btnController.setText("START");
        enableTexts(true);
        resetViews();
    }

    private void resetViews() {
        txtSmallBreakLeft.setText("Small break");
        txtLunchBreakLeft.setText("Lunch break");
    }

    public void setBtnTextToStop() {
        this.btnController.setText("RESET");
    }

    public void updateTimerText(int i) {
        int minutes = i / 60;
        int seconds = i % 60;
        String timerText = String.format(
                Locale.getDefault(), "%02d:%02d",
                minutes, seconds);
        txtSmallBreakLeft.setText(timerText);
    }


    public void enableTexts(boolean toBeEnabled) {
        txtLunchBreakLeft.setEnabled(toBeEnabled);
        txtSmallBreakLeft.setEnabled(toBeEnabled);
    }

    public void updateViewsOnTextClick(String breakName, int seconds) {
        txtLunchBreakLeft.setText(breakName);
        updateTimerText(seconds);
        btnController.setEnabled(true);
    }

    public void updateViewsOnFinish() {
        txtSmallBreakLeft.setText("End");
        btnController.setText("Restart");
    }
}
