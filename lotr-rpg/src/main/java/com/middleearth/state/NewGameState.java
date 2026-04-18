package com.middleearth.state;

import com.middleearth.db.CharacterClassRepository;
import com.middleearth.db.InventoryItemRepository;
import com.middleearth.db.PlayerRepository;
import com.middleearth.engine.CharacterClass;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.items.Equipable;
import com.middleearth.items.Item;
import com.middleearth.ui.CommandInterceptor;
import com.middleearth.ui.Renderer;

import java.util.ArrayList;
import java.util.List;

public class NewGameState implements GameState {

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("Create Your Hero");
        ui.renderSubtitle("Choose Your Name");

        ui.render("Every great journey begins with a name.");
        ui.render("Choose wisely - this is how Middle-earth will remember you.");

        String name = ui.prompt("Enter your hero's name");

        if (name.startsWith(":")) {
            return CommandInterceptor.handle(name, this);
        }

        if (name.isEmpty()) {
            ui.addFlashError("A hero must have a name!");
            return this;
        }

        // --- Class selection ---
        CharacterClassRepository classRepo = new CharacterClassRepository();
        List<CharacterClass> classes = classRepo.getAll();

        ui.renderSubtitle("Choose Your Class");

        List<String> options = new ArrayList<>();
        for (CharacterClass cc : classes) {
            Item weapon = cc.getStarterWeapon();
            Item item = cc.getStarterItem();
            String weaponInfo = weapon != null ? weapon.getName() : "None";
            if (weapon instanceof Equipable) {
                weaponInfo += " (dmg " + ((Equipable) weapon).getBonus() + ")";
            }
            String itemInfo = item != null ? item.getName() : "None";

            options.add(Renderer.Style.GOLD + "" + cc.getName() + "" + Renderer.Style.RESET
                    + Renderer.Style.GRAY + "" + "\n      " + cc.getDescription()  + Renderer.Style.RESET
                    + "\n      HP: " + cc.getBaseHealth()
                    + "  |  Bag: " + (int) cc.getBagCapacity()
                    + "  |  Weapon: " + weaponInfo
                    + "  |  Item: " + itemInfo
                    + "\n");
        }
        ui.renderOptions(options);

        String choice = ui.prompt("Pick a class: ");

        if (choice.startsWith(":")) {
            return CommandInterceptor.handle(choice, this);
        }

        int classIndex;
        try {
            classIndex = Integer.parseInt(choice) - 1;
        } catch (NumberFormatException e) {
            ui.addFlashError("Invalid choice.");
            return this;
        }

        if (classIndex < 0 || classIndex >= classes.size()) {
            ui.addFlashError("Invalid choice.");
            return this;
        }

        CharacterClass chosen = classes.get(classIndex);

        Player player = new Player(name, chosen);

        // Persist to database
        PlayerRepository repo = new PlayerRepository();
        repo.insert(player);

        // Save starter items to the database
        if (player.getId() > 0) {
            InventoryItemRepository inventoryRepo = new InventoryItemRepository();

            if (chosen.getStarterWeapon() != null) {
                inventoryRepo.addItemToPlayer(player.getId(), chosen.getStarterWeapon().getId());
            }
            if (chosen.getStarterItem() != null) {
                inventoryRepo.addItemToPlayer(player.getId(), chosen.getStarterItem().getId());
            }
        }

        if (player.getId() > 0) {
            ui.addFlashInfo("Hero '" + name + "' the " + chosen.getName() + " saved to the chronicles.");
        } else {
            ui.addFlashInfo("Hero '" + name + "' created (not persisted — no database).");
        }

        // Initialize the game session with the new player
        GameSession.init(player);

        return new com.middleearth.state.CharacterSelectState();
    }
}
