package com.middleearth;

import com.middleearth.models.characters.GameCharacter;

public class App {
    public static void main(String[] args) {
        GameCharacter gc1 = new GameCharacter(1, "Aragorn", 100, 100, 15, 10);
        GameCharacter gc2 = new GameCharacter(2, "Orc", 80, 80, 10, 5);

        System.out.println(gc1);
        System.out.println(gc2);

        gc1.attack(gc2);

        System.out.println("After attack:");
        System.out.println(gc1);
        System.out.println(gc2);
    }
}
