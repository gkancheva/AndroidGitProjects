package com.gkancheva.connecttree;

public class Game {
    private static final String YELLOW_PLAYER = "Yellow";
    private static final String RED_PLAYER = "Red";

    private Board board;
    private boolean gameIsActive;
    private String activePlayer;

    public Game(Board board) {
        this.board = board;
        this.gameIsActive = true;
        this.activePlayer = YELLOW_PLAYER;
    }

    public String checkForWinner() {
        return this.board.checkForLine();
    }

    public boolean gameIsOver() {
        return this.board.allPositionsArePlayed();
    }

    public void restartGame() {
        this.board.emptyBoard();
        this.activePlayer = YELLOW_PLAYER;
        this.gameIsActive = true;
    }

    public boolean playPosition(int position) {
        if(this.board.positionIsNotPlayed(position) && this.gameIsActive) {
            this.board.setPlayedPosition(activePlayer, position);
            if(activePlayer.equals(YELLOW_PLAYER)) {
                activePlayer = RED_PLAYER;
            } else {
                activePlayer = YELLOW_PLAYER;
            }
            return true;
        }
        return false;
    }

    public void setGameIsActive(boolean gameIsActive) {
        this.gameIsActive = gameIsActive;
    }

    public String getActivePlayer() {
        return activePlayer;
    }
}
