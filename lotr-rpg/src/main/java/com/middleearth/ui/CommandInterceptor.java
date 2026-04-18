package com.middleearth.ui;

import com.middleearth.state.GameState;

import com.middleearth.state.InventoryState;

public class CommandInterceptor {

    /**
     * Checks if the input is a global command. 
     * Returns the new GameState if a transition needs to happen.
     * Returns null if it's NOT a global command.
     */
    public static GameState handle(String input, GameState currentState) {
        Renderer ui = Renderer.getInstance();

        switch (input.toLowerCase()) {
            case ":h":
                // We use our flash queue from earlier!
                ui.addFlashInfo("Global Commands \n :h (help)\n :i (inventory)\n :r (refresh) \n :q (quit)");
                return currentState; // Return the exact same state to reload the screen

            case ":i":
                return new InventoryState(currentState);

            case ":r":
                return currentState;

            case ":q":
                ui.render("Farewell, traveler.", Renderer.Style.GOLD);
                System.exit(0);
                return null;

            default:
                ui.addFlashError("Unknown global command. Type :h for a list.");
                return currentState;
        }
    }
}