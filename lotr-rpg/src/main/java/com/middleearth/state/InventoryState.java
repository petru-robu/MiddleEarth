package com.middleearth.state;

import com.middleearth.db.InventoryItemRepository;
import com.middleearth.ui.Renderer;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.items.Consumable;
import com.middleearth.items.Equipable;
import com.middleearth.items.EquipmentSlot;
import com.middleearth.items.Item;

import java.util.ArrayList;
import java.util.List;

import com.middleearth.ui.CommandInterceptor;

public class InventoryState implements GameState {
    private final GameState previousState;
    private final InventoryItemRepository inventoryRepo = new InventoryItemRepository();

    public InventoryState(GameState previousState) {
        this.previousState = previousState;
    }

    /**
     * Translates an Item object into a clean, formatted string for the UI.
     */
    private String formatItemForList(Item item) {
        StringBuilder sb = new StringBuilder();

        // 1. The Name
        sb.append(item.getName());

        // 2. The Stats (Dynamically checked via interfaces!)
        if (item instanceof Equipable) {
            Equipable eq = (Equipable) item;
            sb.append(" [").append(eq.getSlot()).append("] ");

            // Just a little logic to distinguish weapons from armor
            if (eq.getSlot() == EquipmentSlot.MAIN_HAND || eq.getSlot() == EquipmentSlot.OFF_HAND) {
                sb.append("- +").append(eq.getBonus()).append(" DMG");
            } else {
                sb.append("- +").append(eq.getBonus()).append(" DEF");
            }
        } else if (item instanceof Consumable) {
            Consumable cons = (Consumable) item;
            sb.append(" [Consumable] - Restores ").append(cons.getRestoreAmount()).append(" HP");
        }

        // 3. The Weight
        sb.append(" (").append(item.getWeight()).append(" kg)");

        return sb.toString();
    }

    @Override
    public GameState update() {

        Renderer ui = Renderer.getInstance();

        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("Player Information & Inventory");

        ui.renderSubtitle("Information: ");
        Player player = GameSession.getInstance().getPlayer();

        ui.render("Player Name: " + player.getName());
        ui.render("Experince Level: " + player.getXp());
        ui.render("Health: " + player.getHealth());

        ui.renderSubtitle("Inventory: ");

        // Get items and weight from the database
        List<Item> items = inventoryRepo.getItemsByPlayerId(player.getId());
        double currentWeight = inventoryRepo.getTotalWeight(player.getId());
        double maxCapacity = player.getCharacterClass() != null
                ? player.getCharacterClass().getBagCapacity()
                : 600;

        String weightText = String.format("Weight: %.1f / %.1f lbs", currentWeight, maxCapacity);
        ui.render(weightText, Renderer.Style.CYAN);

        if (items.isEmpty()) {
            ui.render("Your bag is entirely empty. Only dust remains.");
            String input = ui.prompt("Type :b to go back");

            if (input.equalsIgnoreCase(":b")) {
                return previousState;
            }

            // handle invetory opening in inventory
            if (input.equalsIgnoreCase(":i")) {
                return this;
            }

            return CommandInterceptor.handle(input, this);
        }

        List<String> displayOptions = new ArrayList<>();
        for (Item item : items) {
            displayOptions.add(formatItemForList(item));
        }

        ui.renderOptions(displayOptions);

        String input = ui.prompt("Select an item, or type :b to go back");

        if (input.startsWith(":")) {
            // handle back locally
            if (input.equalsIgnoreCase(":b")) {
                ui.addFlashInfo("You closed your inventory.");
                return previousState;
            }

            // handle invetory opening in inventory
            if (input.equalsIgnoreCase(":i")) {
                return this;
            }

            return CommandInterceptor.handle(input, this);
        }

        switch (input) {
            default:
                ui.addFlashError("Invalid item selection.");
                return this;
        }
    }
}