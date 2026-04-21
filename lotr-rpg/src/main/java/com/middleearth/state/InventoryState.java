package com.middleearth.state;

import com.middleearth.db.InventoryItemRepository;
import com.middleearth.db.PlayerRepository;
import com.middleearth.ui.Renderer;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.items.Consumable;
import com.middleearth.items.Equipable;
import com.middleearth.items.EquipmentSlot;
import com.middleearth.items.Item;

import java.util.ArrayList;
import java.util.List;

import com.middleearth.service.AuditService;
import com.middleearth.ui.CommandInterceptor;

public class InventoryState implements GameState {
    private final GameState previousState;
    private final InventoryItemRepository inventoryRepo = new InventoryItemRepository();

    public InventoryState(GameState previousState) {
        this.previousState = previousState;
    }

    private String formatItemForList(Item item, Player player) {
        StringBuilder sb = new StringBuilder();

        // Show equipped indicator
        boolean isEquipped = (player.getEquippedWeapon() != null && player.getEquippedWeapon().getId() == item.getId())
            || (player.getEquippedArmor() != null && player.getEquippedArmor().getId() == item.getId());
        if (isEquipped) {
            sb.append(Renderer.Style.GREEN).append("[E] ").append(Renderer.Style.RESET);
        }

        sb.append(item.getName());

        if (item instanceof Equipable) {
            Equipable eq = (Equipable) item;
            sb.append(" [").append(eq.getSlot()).append("] ");
            if (Player.isWeaponSlot(eq.getSlot())) {
                sb.append("- +").append(eq.getBonus()).append(" ATK");
            } else {
                sb.append("- +").append(eq.getBonus()).append(" DEF");
            }
        } else if (item instanceof Consumable) {
            sb.append(" [Consumable] - Restores ").append(((Consumable) item).getRestoreAmount()).append(" HP");
        }

        sb.append(" (").append(item.getWeight()).append(" kg)");
        return sb.toString();
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();
        AuditService.getInstance().log(AuditService.VIEW_INVENTORY);
        ui.clear();
        ui.renderFlashes();
        ui.renderTitle("Player Information & Inventory");

        ui.renderSubtitle("Information: ");
        Player player = GameSession.getInstance().getPlayer();

        int maxHp = player.getCharacterClass() != null ? player.getCharacterClass().getBaseHealth() : 100;
        int atk   = player.getAttackBonus();
        int def   = player.getDefenseBonus();

        ui.render("  Name:   " + player.getName());
        ui.render("  HP:     " + ui.bar(player.getHealth(), maxHp, 20, Renderer.Style.GREEN) +
            "  " + player.getHealth() + " / " + maxHp);
        ui.render("  ATK:    " + ui.bar(atk, Player.MAX_ATTACK, 20, Renderer.Style.RED) +
            "  " + atk + " / " + Player.MAX_ATTACK);
        ui.render("  DEF:    " + ui.bar(def, Player.MAX_DEFENSE, 20, Renderer.Style.CYAN) +
            "  " + def + " / " + Player.MAX_DEFENSE);
        ui.render("  XP:     " + Renderer.Style.GOLD + player.getXp() + " XP" + Renderer.Style.RESET);

        // Equipped items summary
        String weaponName = player.getEquippedWeapon() != null ? player.getEquippedWeapon().getName()
            : (player.getCharacterClass() != null && player.getCharacterClass().getStarterWeapon() != null
               ? player.getCharacterClass().getStarterWeapon().getName() + " (default)" : "none");
        String armorName = player.getEquippedArmor() != null ? player.getEquippedArmor().getName() : "none";
        ui.render("  Weapon: " + Renderer.Style.GOLD + weaponName + Renderer.Style.RESET);
        ui.render("  Armor:  " + Renderer.Style.GOLD + armorName + Renderer.Style.RESET);

        ui.renderSubtitle("Inventory: ");

        List<Item> items = inventoryRepo.getItemsByPlayerId(player.getId());
        double currentWeight = inventoryRepo.getTotalWeight(player.getId());
        double maxCapacity = player.getCharacterClass() != null
                ? player.getCharacterClass().getBagCapacity()
                : 600;

        ui.render("  Weight: " + ui.bar((int) currentWeight, (int) maxCapacity, 20, Renderer.Style.CYAN) +
            String.format("  %.1f / %.1f kg", currentWeight, maxCapacity));

        if (items.isEmpty()) {
            ui.render("Your bag is entirely empty. Only dust remains.");
            String input = ui.prompt("Type :b to go back");
            if (input.equalsIgnoreCase(":b")) return previousState;
            if (input.equalsIgnoreCase(":i")) return this;
            return CommandInterceptor.handle(input, this);
        }

        List<String> displayOptions = new ArrayList<>();
        for (Item item : items) {
            displayOptions.add(formatItemForList(item, player));
        }

        ui.renderOptions(displayOptions);
        String input = ui.prompt("Select an item number, or :b to go back");

        if (input.startsWith(":")) {
            if (input.equalsIgnoreCase(":b")) {
                ui.addFlashInfo("You closed your inventory.");
                return previousState;
            }
            if (input.equalsIgnoreCase(":i")) return this;
            return CommandInterceptor.handle(input, this);
        }

        try {
            int choice = Integer.parseInt(input.trim());
            if (choice >= 1 && choice <= items.size()) {
                Item selected = items.get(choice - 1);
                handleItemAction(ui, player, selected);
            } else {
                ui.addFlashError("Invalid item selection.");
            }
        } catch (NumberFormatException e) {
            ui.addFlashError("Invalid item selection.");
        }

        return this;
    }

    private void handleItemAction(Renderer ui, Player player, Item selected) {
        boolean isEquippedWeapon = player.getEquippedWeapon() != null
            && player.getEquippedWeapon().getId() == selected.getId();
        boolean isEquippedArmor = player.getEquippedArmor() != null
            && player.getEquippedArmor().getId() == selected.getId();

        if (selected instanceof Equipable) {
            Equipable eq = (Equipable) selected;
            boolean isEquipped = isEquippedWeapon || isEquippedArmor;

            if (isEquipped) {
                // Show equip/unequip choice
                ui.render("\n  " + Renderer.Style.GOLD + selected.getName() + Renderer.Style.RESET + " is currently equipped.\n");
                ui.render("  " + Renderer.Style.CYAN + "1) " + Renderer.Style.RESET + "Unequip");
                ui.render("  " + Renderer.Style.CYAN + "2) " + Renderer.Style.RESET + "Cancel");
                String action = ui.prompt("Action").trim();
                if (action.equals("1")) {
                    if (isEquippedWeapon) {
                        player.setEquippedWeapon(null);
                        AuditService.getInstance().log(AuditService.UNEQUIP_WEAPON);
                    } else {
                        player.setEquippedArmor(null);
                        AuditService.getInstance().log(AuditService.UNEQUIP_ARMOR);
                    }
                    new PlayerRepository().update(player);
                    ui.addFlashInfo("Unequipped: " + selected.getName());
                }
            } else {
                // Show equip choice
                ui.render("\n  " + Renderer.Style.GOLD + selected.getName() + Renderer.Style.RESET + "\n");
                ui.render("  " + Renderer.Style.CYAN + "1) " + Renderer.Style.RESET + "Equip");
                ui.render("  " + Renderer.Style.CYAN + "2) " + Renderer.Style.RESET + "Cancel");
                String action = ui.prompt("Action").trim();
                if (action.equals("1")) {
                    if (Player.isWeaponSlot(eq.getSlot())) {
                        player.setEquippedWeapon(selected);
                        AuditService.getInstance().log(AuditService.EQUIP_WEAPON);
                        ui.addFlashInfo("Equipped weapon: " + selected.getName() + " (+" + eq.getBonus() + " ATK)");
                    } else {
                        player.setEquippedArmor(selected);
                        AuditService.getInstance().log(AuditService.EQUIP_ARMOR);
                        ui.addFlashInfo("Equipped armor: " + selected.getName() + " (+" + eq.getBonus() + " DEF)");
                    }
                    new PlayerRepository().update(player);
                }
            }

        } else if (selected instanceof Consumable) {
            Consumable cons = (Consumable) selected;
            int maxHp = player.getCharacterClass() != null ? player.getCharacterClass().getBaseHealth() : 100;

            ui.render("\n  " + Renderer.Style.GOLD + selected.getName() + Renderer.Style.RESET +
                " — restores " + cons.getRestoreAmount() + " HP");
            ui.render("  Your HP: " + player.getHealth() + " / " + maxHp);
            ui.render("  " + Renderer.Style.CYAN + "1) " + Renderer.Style.RESET + "Use");
            ui.render("  " + Renderer.Style.CYAN + "2) " + Renderer.Style.RESET + "Cancel");
            String action = ui.prompt("Action").trim();
            if (action.equals("1")) {
                int newHp = Math.min(player.getHealth() + cons.getRestoreAmount(), maxHp);
                int gained = newHp - player.getHealth();
                player.setHealth(newHp);
                new PlayerRepository().update(player);
                inventoryRepo.removeItemFromPlayer(player.getId(), selected.getId());
                AuditService.getInstance().log(AuditService.CONSUME_ITEM);
                ui.addFlashInfo(cons.getUseMessage() + " (+" + gained + " HP)");
            }
        } else {
            ui.addFlashError(selected.getName() + " has no available action.");
        }
    }
}
