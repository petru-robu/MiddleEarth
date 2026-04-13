package com.middleearth.state.game;

import com.middleearth.CommandInterceptor;
import com.middleearth.Renderer;
import com.middleearth.engine.Area;
import com.middleearth.engine.GameSession;
import com.middleearth.state.GameState;
import com.middleearth.engine.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenWorld implements GameState {

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

        GameSession game = GameSession.getInstance();
        Player player = game.getPlayer();
        List<Area> worldMap = game.getWorldMap();

        ui.renderAreaOptions(worldMap, player.getXp());

        String input = ui.prompt("Choose a destination: ");

        if (input.startsWith(":")) {
            // pass to interceptor
            return CommandInterceptor.handle(input, this);
        }

        try{
            Integer.parseInt(input);
        } catch(NumberFormatException e) {
            ui.addFlashError("Invalid choice.");
            return this;
        
        }
        
        int index = Integer.parseInt(input) - 1;

        if(index < 0 || index >= worldMap.size()) {
            ui.addFlashError("Invalid choice.");
            return this;
        }
        Area selected = worldMap.get(index);

        if(!selected.isUnlocked(player.getXp())) {
            ui.addFlashError("You cannot enter " + selected.getName() + " yet!");
            return this;
        }

        switch (input) {
            case "1":
                return new com.middleearth.state.regions.shire.Shire();

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
