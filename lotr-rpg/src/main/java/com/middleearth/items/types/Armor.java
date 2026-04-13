package com.middleearth.items.types;

import com.middleearth.items.AbstractItem;
import com.middleearth.items.Equipable;
import com.middleearth.items.EquipmentSlot;

public class Armor extends AbstractItem implements Equipable {
    private final EquipmentSlot slot;
    private final int defense;

    public Armor(String name, String description, double weight, EquipmentSlot slot, int defense) {
        super(name, description, weight);
        this.slot = slot;
        this.defense = defense;
    }

    @Override
    public EquipmentSlot getSlot() {
        return slot;
    }

    @Override
    public int getBonus() {
        return defense;
    }
}