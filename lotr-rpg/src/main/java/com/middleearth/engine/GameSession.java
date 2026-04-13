package com.middleearth.engine;

import java.util.List;

import com.middleearth.items.types.Potion;
import com.middleearth.items.types.Weapon;

public class GameSession {
    private static GameSession instance;
    private Player currentPlayer;
    private List<Area> worldMap;

    private GameSession() {
        // Initialize Player
       currentPlayer = new Player("Aragorn");

        Weapon rangerSword = new Weapon("Ranger's Longsword", "A well-worn but sharp blade.", 4.5, 12, false);
        Potion lembas = new Potion("Lembas Bread", "One bite is enough to fill the stomach of a grown man.", 0.5, 50,
                "You take a bite. You feel invigorated!");

        currentPlayer.getInventory().addItem(rangerSword);

        currentPlayer.getInventory().addItem(lembas);

        currentPlayer.addXp(26);

        // Initialize world map with areas and their XP requirements
        worldMap = new java.util.ArrayList<>();
        worldMap.add(new Area(
                "The Shire",
                "Rolling green hills and quiet hobbit holes. A peaceful place to start.",
                0));

        worldMap.add(new Area(
                "Bree",
                "A bustling crossroads town. Watch your pockets at the Prancing Pony.",
                25));

        worldMap.add(new Area(
                "Rivendell",
                "The Last Homely House east of the Sea. Home to Elrond and ancient wisdom.",
                100));

        worldMap.add(new Area(
                "Lothlórien",
                "The golden forest of the Galadhrim. Enchanted, beautiful, and dangerous.",
                250));

        worldMap.add(new Area(
                "The Black Gate",
                "The jagged iron entrance to Mordor. Only the bravest (or most foolish) approach.",
                500));
    }

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public Player getPlayer() {
        return currentPlayer;
    }

    public List<Area> getWorldMap() {
        return worldMap;
    }
}