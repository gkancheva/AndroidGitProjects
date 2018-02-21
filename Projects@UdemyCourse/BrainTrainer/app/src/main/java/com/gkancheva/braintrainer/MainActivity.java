package com.gkancheva.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private GameView gameView;

    public void start(View view) {
        gameView.startGame();
        startTimer();
    }

    private void startTimer() {
        new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long secondsLeft) {
                gameView.updateTimerTextView((int)secondsLeft / 1000);
            }

            @Override
            public void onFinish() {
                gameView.onGameOver(game.getFinalScore());
                gameView.setNewQuestion(
                        game.startGame(), game.getAnswers());
            }
        }.start();
    }

    public void onOptionChosen(View view) {
        int tag = Integer.parseInt((String)view.getTag());
        gameView.onOptionChosen(game.isCorrectAnswer(tag));
        gameView.updateScoreTextView(game.getCurrentScore());
        gameView.setNewQuestion(
                game.generateQuestion(),
                game.getAnswers());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = new GameView(this);
        game = new Game();
        gameView.setNewQuestion(
                game.startGame(),
                game.getAnswers());
    }

}