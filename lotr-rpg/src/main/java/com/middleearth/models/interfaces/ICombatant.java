package com.middleearth.models.interfaces;

public interface ICombatant {
    void attack(ICombatant target);
    void takeDamage(int amount);
    boolean isAlive();
}