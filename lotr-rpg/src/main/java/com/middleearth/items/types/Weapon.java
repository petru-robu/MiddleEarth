package com.middleearth.items.types;

import com.middleearth.items.AbstractItem;
import com.middleearth.items.Equipable;
import com.middleearth.items.EquipmentSlot;

public class Weapon extends AbstractItem implements Equipable {
    private final int damage;
    private final boolean twoHanded;

    public Weapon(String name, String description, double weight, int damage, boolean twoHanded) {
        super(name, description, weight);
        this.damage = damage;
        this.twoHanded = twoHanded;
    }

    @Override
    public EquipmentSlot getSlot() {
        return EquipmentSlot.MAIN_HAND;
    }

    @Override
    public int getBonus() {
        return damage;
    }

    public boolean isTwoHanded() {
        return twoHanded;
    }
}