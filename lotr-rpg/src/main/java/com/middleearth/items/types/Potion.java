package com.middleearth.items.types;

import com.middleearth.items.AbstractItem;
import com.middleearth.items.Consumable;

public class Potion extends AbstractItem implements Consumable {
    private final int healAmount;
    private final String useMessage;

    public Potion(String name, String description, double weight, int healAmount, String useMessage) {
        super(name, description, weight);
        this.healAmount = healAmount;
        this.useMessage = useMessage;
    }

    @Override
    public int getRestoreAmount() { return healAmount; }

    @Override
    public String getUseMessage() { return useMessage; }
}