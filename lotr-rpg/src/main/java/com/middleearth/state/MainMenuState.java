package com.middleearth.state;

import com.middleearth.ui.CommandInterceptor;
import com.middleearth.ui.Renderer;

import java.util.List;

public class MainMenuState implements GameState {
    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("The Lord of the Rings");

        List<String> options = List.of("Play", "Exit");
        ui.renderOptions(options);

        String input = ui.prompt("Choose an option");
        
        // delegate to interceptor
        if (input.startsWith(":")) {
            return CommandInterceptor.handle(input, this);
        }

        switch (input) {
            case "1":
                return new CharacterSelectState();
            case "2":
                System.exit(0);
            default:
                ui.addFlashError("Invalid choice.");
                return this;
        }
    }
}