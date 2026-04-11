package com.middleearth.engine;

public class Player {
    private String name;
    private int health;
    private Inventory inventory;

    public Player(String name) {
        this.name = name;

        inventory = new Inventory(600);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Inventory getInventory() {
        return inventory;
    }

}