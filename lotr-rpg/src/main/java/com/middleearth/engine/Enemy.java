package com.middleearth.engine;

import com.middleearth.items.Item;

public class Enemy {

    public enum EnemyType {
        RUFFIAN, WOLF, UNDEAD, ORC, TROLL
    }

    private int id;
    private int questId;
    private String name;
    private EnemyType type;
    private int maxHealth;
    private int health;
    private int attack;
    private Item lootItem; // nullable — dropped when killed
    private String asciiArt;

    public Enemy(String name, EnemyType type, int health, int attack) {
        this.name = name;
        this.type = type;
        this.maxHealth = health;
        this.health = health;
        this.attack = attack;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuestId() { return questId; }
    public void setQuestId(int questId) { this.questId = questId; }

    public String getName() { return name; }
    public EnemyType getType() { return type; }

    public int getMaxHealth() { return maxHealth; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public boolean isAlive() { return health > 0; }

    public int getAttack() { return attack; }

    public Item getLootItem() { return lootItem; }
    public void setLootItem(Item lootItem) { this.lootItem = lootItem; }

    public String getAsciiArt() { return asciiArt; }
    public void setAsciiArt(String asciiArt) { this.asciiArt = asciiArt; }
}
