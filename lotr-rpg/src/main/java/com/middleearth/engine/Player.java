package com.middleearth.engine;

import com.middleearth.db.PlayerRepository;

public class Player {
    private int id;
    private String name;
    private int health;
    private int xp = 0;
    private CharacterClass characterClass;

    public Player(String name) {
        this.name = name;
        this.health = 100;
    }

    public Player(String name, CharacterClass characterClass) {
        this.name = name;
        this.characterClass = characterClass;
        this.health = characterClass.getBaseHealth();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getXp() {
        return xp;
    }

    public void addXp(int amount) {
        this.xp += amount;
        if (this.id > 0) {
            new PlayerRepository().update(this);
        }
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

}