package com.middleearth.items;

public abstract class AbstractItem implements Item {
    private final String name;
    private final String description;
    private final double weight;

    public AbstractItem(String name, String description, double weight) {
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}