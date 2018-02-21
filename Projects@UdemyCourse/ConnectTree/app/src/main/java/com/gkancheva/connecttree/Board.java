package com.gkancheva.connecttree;

public class Board {
    private static final String UNPLAYED_POINT = "";

    private String[] statistics;
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1 , 4, 7}, {2, 5, 8},
            {0 , 4, 8}, {2, 4, 6}};

    public Board() {
        this.statistics = new String[9];
        this.emptyBoard();
    }

    public void setPlayedPosition(String player, int index) {
        this.statistics[index] = player;
    }

    public boolean positionIsNotPlayed(int index) {
        return this.statistics[index].equals(UNPLAYED_POINT);
    }

    public String checkForLine() {
        for (int[] winningArr : this.winningPositions) {
            String currentWinner = this.statistics[winningArr[0]];
            if(!currentWinner.equals(UNPLAYED_POINT)) {
                if(currentWinner.equals(this.statistics[winningArr[1]]) &&
                        currentWinner.equals(this.statistics[winningArr[2]])) {
                    return currentWinner;
                }
            }
        }
        return "";
    }

    public void emptyBoard() {
        for (int i = 0; i < this.statistics.length; i++) {
            this.statistics[i] = UNPLAYED_POINT;
        }
    }

    public boolean allPositionsArePlayed() {
        for (String statistic : this.statistics) {
            if (statistic.equals(UNPLAYED_POINT)) {
                return false;
            }
        }
        return true;
    }
}
