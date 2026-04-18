package com.middleearth.state;

import com.middleearth.db.PlayerRepository;
import com.middleearth.engine.CharacterClass;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.ui.CommandInterceptor;
import com.middleearth.ui.Renderer;

import java.util.ArrayList;
import java.util.List;

public class CharacterSelectState implements GameState {

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("Character Select");

        PlayerRepository repo = new PlayerRepository();
        List<Player> heroes = repo.getAll();

        if (heroes.isEmpty()) {
            ui.render("No heroes found. Create your first one!");
        } else {
            ui.renderSubtitle("Your Heroes");
            List<String> options = new ArrayList<>();
            for (Player p : heroes) {
                CharacterClass cc = p.getCharacterClass();
                String className = cc != null ? cc.getName() : "Unknown";
                options.add(p.getName() + " the " + className + "  [XP: " + p.getXp() + " | HP: " + p.getHealth() + "]");
            }
            ui.renderOptions(options);
        }

        ui.render("");
        int createIndex = heroes.size() + 1;
        ui.render("  " + Renderer.Style.CYAN + createIndex + ") " + Renderer.Style.RESET + Renderer.Style.GREEN + "+ Create New Hero" + Renderer.Style.RESET);

        String input = ui.prompt("Choose a hero or create a new one");

        if (input.startsWith(":")) {
            if (input.equalsIgnoreCase(":b")) {
                return new MainMenuState();
            }
            return CommandInterceptor.handle(input, this);
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            ui.addFlashError("Invalid choice.");
            return this;
        }

        if (choice == createIndex) {
            return new NewGameState();
        }

        // Select existing hero
        int index = choice - 1;
        if (index < 0 || index >= heroes.size()) {
            ui.addFlashError("Invalid choice.");
            return this;
        }

        Player selected = heroes.get(index);
        GameSession.init(selected);
        ui.addFlashInfo("Welcome back, " + selected.getName() + "!");

        return new com.middleearth.state.game.OpenWorld(new MainMenuState());
    }
}
