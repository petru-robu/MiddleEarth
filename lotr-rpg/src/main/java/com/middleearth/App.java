package com.middleearth;

import com.middleearth.db.DatabaseConfiguration;
import com.middleearth.db.migration.MigrationManager;
import com.middleearth.state.GameState;
import com.middleearth.state.MainMenuState;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        // Initialize database and run migrations
        try {
            new MigrationManager().migrate();
        } catch (SQLException e) {
            System.err.println("[DB] Could not connect to database. Running without persistence.");
            System.err.println("[DB] " + e.getMessage());
        }

        // Start the game at the Main Menu
        GameState currentState = new MainMenuState();

        while (currentState != null) {
            currentState = currentState.update();
        }

        DatabaseConfiguration.close();
    }
}
