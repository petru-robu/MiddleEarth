package com.middleearth.engine;

public class GameSession {
    private static GameSession instance;
    private Player currentPlayer;

    private GameSession() {}

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public Player getPlayer() {
        return currentPlayer;
    }

    public Player setPlayer(Player player) {
        this.currentPlayer = player;
        return currentPlayer;
    }
}