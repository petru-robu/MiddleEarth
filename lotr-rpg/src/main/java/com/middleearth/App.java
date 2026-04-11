package com.middleearth;

import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.items.types.Potion;
import com.middleearth.items.types.Weapon;
import com.middleearth.state.GameState;
import com.middleearth.state.MainMenuState;

public class App {
    public static void main(String[] args) {
        // Start the game at the Main Menu
        GameState currentState = new MainMenuState();

        Player player = new Player("Aragorn");

        Weapon rangerSword = new Weapon("Ranger's Longsword", "A well-worn but sharp blade.", 4.5, 12, false);
        Potion lembas = new Potion("Lembas Bread", "One bite is enough to fill the stomach of a grown man.", 0.5, 50, "You take a bite. You feel invigorated!");
        
        player.getInventory().addItem(rangerSword);
        player.getInventory().addItem(lembas);


        GameSession.getInstance().setPlayer(player);

        while (currentState != null) {
            currentState = currentState.update();
        }
    }
}
