package com.middleearth.state.regions.shire;

import com.middleearth.Renderer;
import com.middleearth.state.GameState;
import com.middleearth.CommandInterceptor;

import java.util.List;

public class Shire implements GameState {
    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        ui.clear();
        ui.renderFlashes();
        ui.renderTitle("Welcome to The Shire");

        ui.renderSubtitle(
                "The Shire is a peaceful region inhabited by hobbits.\"It's known for its lush green fields, cozy hobbit holes, and vibrant community spirit.");
        ui.render(
                "\nAs a visitor to the Shire, you can enjoy the simple pleasures of life, such as gardening, farming, and socializing with the friendly hobbits." +
                        "The Shire is also famous for its festivals and celebrations, where you can join in the fun and experience the warmth of hobbit hospitality.");
        
        ui.render("\nWhat would you like to do?");

        List<String> options = List.of(
                "Explore the Shire",
                "Visit Hobbiton",
                "The Crop Raider's Folly",
                "Ambush at the Brandywine (Battle)",
                "Return to Open World");

        ui.renderOptions(options);
        String input = ui.prompt("Choose an action");

        if (input.startsWith(":")) {
            // pass to interceptor
            return CommandInterceptor.handle(input, this);
        }

        switch (input) {
            case "1":
                return this;
            case "2":
                return this;
            case "3":
                return this;
            case "4":
                return this;
            case "5":
                return new com.middleearth.state.game.OpenWorld(this);
            default:
                ui.addFlashError("Invalid choice.");
                return this;
        }
    }

}
