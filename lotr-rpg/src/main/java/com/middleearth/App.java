package com.middleearth;

import com.middleearth.state.GameState;
import com.middleearth.state.MainMenuState;

public class App {
    public static void main(String[] args) {
        // Start the game at the Main Menu
        GameState currentState = new MainMenuState();

        while (currentState != null) {
            currentState = currentState.update();
        }
    }
}
