package com.middleearth.models.characters;

import com.middleearth.models.interfaces.ICombatant;

public class GameCharacter implements ICombatant {
    protected int id;
    protected String name;
    protected int hp;
    protected int maxHp;
    protected int baseAttack;
    protected int speed;

    // Constructor
    public GameCharacter(int id, String name, int hp, int maxHp, int baseAttack, int speed) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.maxHp = maxHp;
        this.baseAttack = baseAttack;
        this.speed = speed;
    }

    public GameCharacter() {
        this.id = 0;
        this.name = "Unnamed";
        this.hp = 100;
        this.maxHp = 100;
        this.baseAttack = 10;
        this.speed = 5;
    }

    // Combat methods
    @Override
    public void attack(ICombatant enemy) {
        enemy.takeDamage(baseAttack);
    }

    @Override
    public void takeDamage(int amount) {
        hp -= amount;
        if (hp < 0) {
            hp = 0;
        }
    }

    @Override
    public boolean isAlive() {
        return (hp > 0);
    }

    // Getters and setters
    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return "GameCharacter {" +
                "name='" + name + '\'' +
                ", maxHp = " + maxHp +
                ", hp=" + hp + 
                ", baseAttack=" + baseAttack +
                ", speed=" + speed +
                '}';
    } 
}
