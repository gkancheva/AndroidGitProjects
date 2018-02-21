package com.gkancheva.connecttree;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gery on 12.5.2017 г..
 */

public class GameView {
    private static final String YELLOW_COUNTER = "Yellow";

    private BoardView boardView;
    private LinearLayout gameLayout;
    private TextView infoTextView;

    public GameView(BoardView boardView, LinearLayout gameLayout, TextView infoTextView) {
        this.boardView = boardView;
        this.gameLayout = gameLayout;
        this.infoTextView = infoTextView;
    }

    public void displayInfo(String message) {
        this.infoTextView.setText(message);
        this.gameLayout.setVisibility(View.VISIBLE);
    }

    public void restartGameLayout() {
        this.gameLayout.setVisibility(View.INVISIBLE);
        this.boardView.emptyBoard();
    }

    public void playPositionOnBoard(ImageView counter, String activePlayer) {
        counter.setTranslationY(-1000f);
        if(activePlayer.equals(YELLOW_COUNTER)) {
            counter.setImageResource(R.drawable.yellow);
        } else {
            counter.setImageResource(R.drawable.red);
        }
        counter.animate()
                .translationYBy(1000f)
                .rotation(36)
                .setDuration(30);
    }

}
