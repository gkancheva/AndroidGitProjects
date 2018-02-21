package com.breakcount;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Toast;

import com.breakcount.model.Break;
import com.breakcount.model.Counter;
import com.breakcount.model.LunchBreak;
import com.breakcount.model.SmallBreak;
import com.breakcount.model.TimerView;
import com.breakcount.services.TimeChangedListener;

public class MainActivity extends Activity
        implements View.OnClickListener, TimeChangedListener {
    private static final String BREAK_FINISHED = "Break finished!";
    private Break breakType;
    private Counter counter;
    private TimerView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.counter = new Counter(this);
        final WatchViewStub stub = findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                timer = new TimerView(stub);
                timer.setOnClickListeners(MainActivity.this);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_start_count) {
            timer.enableTexts(false);
            if(!counter.isActive()) {
                counter.setIsActive(true);
                timer.setBtnTextToStop();
                counter.startTimer(breakType.getSeconds());
            } else {
                counter.setIsActive(false);
                timer.resetTimer();
                counter.cancelTimer();
            }
            return;
        } else if(view.getId() == R.id.txt_small_break) {
            breakType = new SmallBreak();
        } else if(view.getId() == R.id.txt_lunch_break) {
            breakType = new LunchBreak();
        }
        timer.updateViewsOnTextClick(breakType.getName(), breakType.getSeconds());
        Toast.makeText(this, breakType.getName() + " clicked!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTime(int secondsLeft) {
        timer.updateTimerText(secondsLeft);
    }

    @Override
    public void onFinish() {
        timer.updateViewsOnFinish();
        Toast.makeText(MainActivity.this, BREAK_FINISHED, Toast.LENGTH_SHORT).show();
    }
}