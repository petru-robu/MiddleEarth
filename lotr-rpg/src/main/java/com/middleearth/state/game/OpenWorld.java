package com.middleearth.state.game;

import com.middleearth.CommandInterceptor;
import com.middleearth.Renderer;
import com.middleearth.state.GameState;

import java.util.List;

public class OpenWorld implements GameState{

    private final GameState previousState;

    public OpenWorld(GameState previousState) {
        this.previousState = previousState;
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();
        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("Open World");
        
        List<String> options = List.of("Shire", "Bree", "Mordor");
        ui.renderOptions(options);

        String input = ui.prompt("Choose an action");
        
        // handle global commands
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
            default:
                ui.addFlashError("Invalid choice.");
                return this;
        }
    }
}
