package com.middleearth.items;

/*
    Equipable items, must have a slot and a bonus.
*/ 
public interface Equipable {
    EquipmentSlot getSlot();
    int getBonus();
}
