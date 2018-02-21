package com.gkancheva.connecttree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private GameView gameView;

    public void dropIn(View view) {
        ImageView counter = (ImageView)view;
        int point = Integer.parseInt(counter.getTag().toString());

        if(game.playPosition(point)) {
            gameView.playPositionOnBoard(counter, game.getActivePlayer());
            String winner = game.checkForWinner();
            if(!winner.equals("")) {
                game.setGameIsActive(false);
                gameView.displayInfo(String.format(Locale.getDefault(), "%s won!", winner));
            } else {
                if(game.gameIsOver()) {
                    game.setGameIsActive(false);
                    gameView.displayInfo(this.getResources().getString(R.string.game_over));
                }
            }
        }
    }

    public void playAgain(View view) {
        game.restartGame();
        gameView.restartGameLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
        RelativeLayout playBoard = (RelativeLayout) findViewById(R.id.playBoard);
        TextView infoTexView = (TextView) findViewById(R.id.infoTextView);
        game = new Game(new Board());
        gameView = new GameView(new BoardView(playBoard), infoLayout, infoTexView);
    }
}