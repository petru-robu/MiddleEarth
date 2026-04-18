package com.middleearth.engine;

public class Region {
    private int id;
    private String name;
    private String description;
    private int xp;

    public Region(String name, String description, int xp) {
        this.name = name;
        this.description = description;
        this.xp = xp;
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

    public String getDescription() {
        return description;
    }

    public int getXp() {
        return xp;
    }

    public boolean isUnlocked(int playerXp) {
        return playerXp >= xp;
    }
}
