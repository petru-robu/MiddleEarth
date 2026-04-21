package com.middleearth.engine;

import com.middleearth.db.PlayerRepository;
import com.middleearth.items.Equipable;
import com.middleearth.items.EquipmentSlot;
import com.middleearth.items.Item;

public class Player implements Comparable<Player>{

    public static final int MAX_ATTACK  = 40;
    public static final int MAX_DEFENSE = 30;

    private int id;
    private String name;
    private int health;
    private int xp = 0;
    private CharacterClass characterClass;

    // currently equipped items (nullable = nothing equipped beyond class starter)
    private Item equippedWeapon; // MAIN_HAND
    private Item equippedArmor;  // HEAD / CHEST / OFF_HAND / FEET

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

    // --- Equipment ---

    public Item getEquippedWeapon() { return equippedWeapon; }
    public void setEquippedWeapon(Item item) { this.equippedWeapon = item; }

    public Item getEquippedArmor() { return equippedArmor; }
    public void setEquippedArmor(Item item) { this.equippedArmor = item; }

    /** Attack bonus: from equipped weapon, or class starter weapon, capped at MAX_ATTACK. */
    public int getAttackBonus() {
        int bonus = 0;
        if (equippedWeapon instanceof Equipable) {
            bonus = ((Equipable) equippedWeapon).getBonus();
        } else if (characterClass != null && characterClass.getStarterWeapon() instanceof Equipable) {
            bonus = ((Equipable) characterClass.getStarterWeapon()).getBonus();
        }
        return Math.min(bonus, MAX_ATTACK);
    }

    /** Defense bonus: from equipped armor, capped at MAX_DEFENSE. */
    public int getDefenseBonus() {
        if (equippedArmor instanceof Equipable) {
            return Math.min(((Equipable) equippedArmor).getBonus(), MAX_DEFENSE);
        }
        return 0;
    }

    /** True if this item slot is a weapon (MAIN_HAND). */
    public static boolean isWeaponSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAIN_HAND;
    }

    @Override
    public int compareTo(Player other) {
        // sort by XP descending
        return Integer.compare(other.getXp(), this.xp);
    }
}