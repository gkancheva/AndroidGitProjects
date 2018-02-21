package com.gkancheva.braintrainer;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;


public class GameView {
    private Activity activity;
    private Button btnStart, btnOption0, btnOption1, btnOption2, btnOption3;
    private TextView sumTxtView, resultTxtView, pointsTxtView, timerTxtView;

    public GameView(Activity activity) {
        this.activity = activity;
        this.initialiseViews();
        this.prepareLayout();
    }

    private void initialiseViews() {
        this.btnStart = (Button) this.activity.findViewById(R.id.btnStart);
        this.btnOption0 = (Button) this.activity.findViewById(R.id.btn0);
        this.btnOption1 = (Button) this.activity.findViewById(R.id.btn1);
        this.btnOption2 = (Button) this.activity.findViewById(R.id.btn2);
        this.btnOption3 = (Button) this.activity.findViewById(R.id.btn3);

        this.resultTxtView = (TextView) this.activity.findViewById(R.id.resultTxtView);
        this.sumTxtView = (TextView) this.activity.findViewById(R.id.sumTxtView);
        this.pointsTxtView = (TextView) this.activity.findViewById(R.id.pointsTxtView);
        this.timerTxtView = (TextView) this.activity.findViewById(R.id.timerTxtView);
    }

    private void prepareLayout() {
        this.btnStart.setVisibility(View.VISIBLE);
        this.resultTxtView.setVisibility(View.INVISIBLE);
        this.initialiseTextOnViews();
        this.enableLayout(false);
    }

    public void startGame() {
        this.btnStart.setVisibility(View.INVISIBLE);
        this.resultTxtView.setVisibility(View.VISIBLE);
        this.initialiseTextOnViews();
        this.enableLayout(true);
    }

    private void initialiseTextOnViews() {
        this.resultTxtView.setText(R.string.choose_option);
        this.timerTxtView.setText(R.string.start_time);
        this.pointsTxtView.setText(R.string.start_points);
    }

    public void onGameOver(String resultMessage) {
        this.enableLayout(false);
        this.resultTxtView.setText(resultMessage);
        this.updateTimerTextView(0);
        this.btnStart.setVisibility(View.VISIBLE);
        this.btnStart.setText(R.string.play_again);
    }

    public void onOptionChosen(boolean isCorrectAnswer) {
        if(isCorrectAnswer) {
            this.resultTxtView.setText(R.string.correct_answer);
        } else {
            this.resultTxtView.setText(R.string.wrong_answer);
        }
    }


    private void enableLayout(boolean b) {
        btnOption0.setEnabled(b);
        btnOption1.setEnabled(b);
        btnOption2.setEnabled(b);
        btnOption3.setEnabled(b);
    }

    private void updateButtonsText(Map<Integer, Integer> answers) {
        btnOption0.setText(String.format(Locale.getDefault(), " %d ",
                answers.get(0)));
        btnOption1.setText(String.format(Locale.getDefault(), " %d ",
                answers.get(1)));
        btnOption2.setText(String.format(Locale.getDefault(), " %d ",
                answers.get(2)));
        btnOption3.setText(String.format(Locale.getDefault(), " %d ",
                answers.get(3)));

    }

    public void setNewQuestion(String question, Map<Integer, Integer> answers) {
        this.sumTxtView.setText(question);
        this.updateButtonsText(answers);
    }

    public void updateTimerTextView(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;
        timerTxtView.setText(String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds));
    }

    public void updateScoreTextView(String result) {
        this.pointsTxtView.setText(result);
    }
}