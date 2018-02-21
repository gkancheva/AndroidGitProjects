package com.gkancheva.connecttree;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BoardView {

    private RelativeLayout playBoard;

    public BoardView(RelativeLayout playBoard) {
        this.playBoard = playBoard;
    }

    public void emptyBoard() {
        for (int i = 0; i < this.playBoard.getChildCount(); i++) {
            ((ImageView) playBoard.getChildAt(i)).setImageResource(0);
        }
    }
}
