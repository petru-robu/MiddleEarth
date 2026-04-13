package com.middleearth.engine;

public class Area {
    private String name;
    private int requiredXp;

    private String description;

    public Area(String name, String description, int requiredXp) {
        this.name = name;
        this.description = description;
        this.requiredXp = requiredXp;
    }

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getRequiredXp() {
        return requiredXp;
    }

    public boolean isUnlocked(int playerXp) {
        return playerXp >= requiredXp;
    }
}