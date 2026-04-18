package com.middleearth.engine;

import java.util.List;

public class GameSession {
    private static GameSession instance;
    private Player currentPlayer;

    private GameSession() {
    }

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public static void init(Player player) {
        instance = new GameSession();
        instance.currentPlayer = player;
    }

    public Player getPlayer() {
        return currentPlayer;
    }
}