package com.middleearth.state;

import com.middleearth.CommandInterceptor;
import com.middleearth.Renderer;
import com.middleearth.state.game.OpenWorld;

import java.util.List;

public class MainMenuState implements GameState {
    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("The Lord of the Rings");

        List<String> options = List.of("Start New Game", "Load Game", "Exit");
        ui.renderOptions(options);

        String input = ui.prompt("Choose an option");
        
        // delegate to interceptor
        if (input.startsWith(":")) {
            return CommandInterceptor.handle(input, this);
        }

        switch (input) {
            case "1":
                ui.addFlashInfo("This is work in progress.");
                return new OpenWorld(this);
            case "2":
                ui.addFlashError("Load not implemented yet.");
                return this;
            case "3":
                System.exit(0);
            default:
                ui.addFlashError("Invalid choice.");
                return this;
        }
    }
}