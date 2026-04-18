package com.middleearth.engine;

import com.middleearth.items.Item;

public class Quest {

    public enum QuestType {
        PUZZLE, BATTLE
    }

    private int id;
    private int regionId;
    private String title;
    private String description;
    private QuestType questType;
    private int difficulty;
    private int xpReward;
    private Item itemReward;

    public Quest(String title, String description, QuestType questType, int difficulty, int xpReward) {
        this.title = title;
        this.description = description;
        this.questType = questType;
        this.difficulty = difficulty;
        this.xpReward = xpReward;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRegionId() { return regionId; }
    public void setRegionId(int regionId) { this.regionId = regionId; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestType getQuestType() { return questType; }
    public int getDifficulty() { return difficulty; }
    public int getXpReward() { return xpReward; }

    public Item getItemReward() { return itemReward; }
    public void setItemReward(Item itemReward) { this.itemReward = itemReward; }
}
