package com.middleearth.engine;

import com.middleearth.items.Item;

public class CharacterClass {
    private int id;
    private String name;
    private String description;
    private int baseHealth;
    private double bagCapacity;
    private Item starterWeapon;
    private Item starterItem;

    public CharacterClass(String name, String description, int baseHealth, double bagCapacity,
                          Item starterWeapon, Item starterItem) {
        this.name = name;
        this.description = description;
        this.baseHealth = baseHealth;
        this.bagCapacity = bagCapacity;
        this.starterWeapon = starterWeapon;
        this.starterItem = starterItem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getBaseHealth() { return baseHealth; }
    public double getBagCapacity() { return bagCapacity; }
    public Item getStarterWeapon() { return starterWeapon; }
    public Item getStarterItem() { return starterItem; }
}
