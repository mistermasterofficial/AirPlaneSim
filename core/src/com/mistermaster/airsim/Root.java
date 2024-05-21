package com.mistermaster.airsim;

public class Root {
    private static Root instance;

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean isRestartGame() {
        return isRestartGame;
    }

    public void setRestartGame(boolean restartGame) {
        isRestartGame = restartGame;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private boolean isGameOver;
    private boolean isRestartGame;
    private int score;


    private Root() {
        isGameOver = false;
        isRestartGame = true;
        score = 0;
    }

    public static Root getInstance() {
        if(instance==null)
            instance = new Root();
        return instance;
    }
}
