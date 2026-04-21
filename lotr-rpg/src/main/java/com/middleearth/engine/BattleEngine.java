package com.middleearth.engine;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.db.EnemyRepository;
import com.middleearth.db.InventoryItemRepository;
import com.middleearth.db.PlayerRepository;
import com.middleearth.items.Item;
import com.middleearth.items.types.Potion;
import com.middleearth.service.AuditService;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Reusable turn-based battle loop. Quest battle states call {@link #run(Quest, GameState)}
 * after displaying their own intro, passing the state to return to on completion.
 */
public class BattleEngine {

    private static final Random RAND = new Random();

    private BattleEngine() {}

    /**
     * Runs the full combat encounter for the given quest, then returns {@code returnTo}.
     */
    public static GameState run(Quest quest, GameState returnTo) {
        Renderer ui = Renderer.getInstance();
        Player player = GameSession.getInstance().getPlayer();

        List<Enemy> enemies = new EnemyRepository().getByQuestId(quest.getId());
        if (enemies.isEmpty()) {
            ui.addFlashError("No enemies found for this battle. Check the database migration.");
            return returnTo;
        }

        int playerAttackBonus = player.getAttackBonus();
        boolean fled = false;

        for (Enemy enemy : enemies) {
            ui.clear();
            ui.renderTitle("⚔  " + enemy.getName() + " appears!");

            // Display ASCII art if available
            String art = enemy.getAsciiArt();
            if (art != null && !art.isEmpty()) {
                ui.render(Renderer.Style.RED + "\n" + art + "\n" + Renderer.Style.RESET);
            }

            ui.render(Renderer.Style.GRAY + "Type: " + enemy.getType() +
                "  |  HP: " + enemy.getHealth() +
                "  |  ATK: " + enemy.getAttack() + Renderer.Style.RESET + "\n");
            ui.prompt("Press ENTER to fight...");

            while (enemy.isAlive() && player.getHealth() > 0) {
                ui.clear();
                ui.renderTitle("Combat: " + player.getName() + " vs " + enemy.getName());

                ui.render("  Your HP:    " + ui.bar(player.getHealth(), player.getCharacterClass().getBaseHealth(), 20, Renderer.Style.GREEN) +
                    "  " + player.getHealth());
                ui.render("  Enemy HP:   " + ui.bar(enemy.getHealth(), enemy.getMaxHealth(), 20, Renderer.Style.RED) +
                    "  " + enemy.getHealth());
                ui.render("");

                List<Item> potions = getPotionsFromInventory(player.getId());
                potions.sort((a, b) -> ((Potion) a).getRestoreAmount() - ((Potion) b).getRestoreAmount());

                ui.render("  " + Renderer.Style.CYAN + "1) " + Renderer.Style.RESET + "Attack");
                if (!potions.isEmpty()) {
                    ui.render("  " + Renderer.Style.CYAN + "2) " + Renderer.Style.RESET +
                        "Use healing item (" + potions.get(0).getName() + ")");
                }
                ui.render("  " + Renderer.Style.CYAN + (potions.isEmpty() ? "2" : "3") + ") " +
                    Renderer.Style.RESET + "Flee");

                String input = ui.prompt("Action").trim();

                if (input.equals("1")) {
                    AuditService.getInstance().log(AuditService.ATTACK);
                    int dmg = RAND.nextInt(8) + 5 + playerAttackBonus;
                    enemy.setHealth(enemy.getHealth() - dmg);
                    ui.render(Renderer.Style.GREEN + "\nYou strike " + enemy.getName() + " for " + dmg + " damage!" + Renderer.Style.RESET);

                    if (enemy.isAlive()) {
                        int rawDmg = RAND.nextInt(enemy.getAttack() / 2 + 1) + enemy.getAttack() / 2;
                        int enemyDmg = Math.max(1, rawDmg - player.getDefenseBonus());
                        player.setHealth(player.getHealth() - enemyDmg);
                        ui.render(Renderer.Style.RED + enemy.getName() + " hits you for " + enemyDmg + " damage!"
                            + (player.getDefenseBonus() > 0 ? " (" + player.getDefenseBonus() + " blocked)" : "")
                            + Renderer.Style.RESET);
                    }

                } else if (input.equals("2") && !potions.isEmpty()) {
                    AuditService.getInstance().log(AuditService.USE_HEALING_ITEM);
                    Potion potion = (Potion) potions.get(0);
                    int healed = potion.getRestoreAmount();
                    int newHp = Math.min(player.getHealth() + healed, player.getCharacterClass().getBaseHealth());
                    player.setHealth(newHp);
                    new InventoryItemRepository().removeItemFromPlayer(player.getId(), potion.getId());
                    ui.render(Renderer.Style.GREEN + "\nYou drink " + potion.getName() + " and restore " + healed + " HP!" + Renderer.Style.RESET);

                    int rawDmg2 = RAND.nextInt(enemy.getAttack() / 2 + 1) + enemy.getAttack() / 2;
                    int enemyDmg = Math.max(1, rawDmg2 - player.getDefenseBonus());
                    player.setHealth(player.getHealth() - enemyDmg);
                    ui.render(Renderer.Style.RED + enemy.getName() + " hits you for " + enemyDmg + " damage!"
                        + (player.getDefenseBonus() > 0 ? " (" + player.getDefenseBonus() + " blocked)" : "")
                        + Renderer.Style.RESET);

                } else {
                    AuditService.getInstance().log(AuditService.FLEE_BATTLE);
                    fled = true;
                    break;
                }

                ui.prompt("Press ENTER to continue...");
            }

            if (fled) break;

            if (player.getHealth() <= 0) {
                player.setHealth(1);
                new PlayerRepository().update(player);
                ui.clear();
                ui.render(Renderer.Style.RED +
                    "\nYou have been defeated by " + enemy.getName() + "!\n" +
                    "You barely crawl away with your life...\n" +
                    Renderer.Style.RESET);
                ui.addFlashError("Defeated! Try again when you've recovered.");
                ui.prompt("Press ENTER to continue...");
                return returnTo;
            }

            ui.clear();
            ui.render(Renderer.Style.GREEN + "\n" + enemy.getName() + " has been slain!\n" + Renderer.Style.RESET);

            if (enemy.getLootItem() != null) {
                Item loot = enemy.getLootItem();
                ui.render("They dropped: " + Renderer.Style.GOLD + loot.getName() + Renderer.Style.RESET +
                    " — " + loot.getDescription());
                String pick = ui.prompt("Pick it up? [y/n]").trim().toLowerCase();
                if (pick.equals("y") || pick.equals("yes")) {
                    AuditService.getInstance().log(AuditService.LOOT_ITEM);
                    new InventoryItemRepository().addItemToPlayer(player.getId(), loot.getId());
                    ui.render(Renderer.Style.GREEN + loot.getName() + " added to your inventory." + Renderer.Style.RESET);
                }
            }

            ui.prompt("Press ENTER to continue...");
        }

        new PlayerRepository().update(player);

        if (fled) {
            ui.addFlashError("You fled from battle. The quest remains incomplete.");
            return returnTo;
        }

        // Victory
        ui.clear();
        ui.renderTitle("Victory!");
        ui.render(Renderer.Style.GREEN +
            "\nAll enemies have been defeated!\n" +
            "You earned " + quest.getXpReward() + " XP!" +
            Renderer.Style.RESET);

        player.addXp(quest.getXpReward());

        if (quest.getItemReward() != null) {
            Item reward = quest.getItemReward();
            ui.render("Quest reward: " + Renderer.Style.GOLD + reward.getName() + Renderer.Style.RESET);
            new InventoryItemRepository().addItemToPlayer(player.getId(), reward.getId());
        }

        new CompletedQuestRepository().markCompleted(player.getId(), quest.getId());
        AuditService.getInstance().log(AuditService.COMPLETE_QUEST);
        ui.addFlashInfo("Quest completed: " + quest.getTitle());
        ui.addFlashInfo("Got " + quest.getXpReward() + " XP!");

        ui.prompt("Press ENTER to continue...");
        return returnTo;
    }

    private static List<Item> getPotionsFromInventory(int playerId) {
        List<Item> all = new InventoryItemRepository().getItemsByPlayerId(playerId);
        List<Item> potions = new ArrayList<>();
        for (Item item : all) {
            if (item instanceof Potion) {
                potions.add(item);
            }
        }
        return potions;
    }
}
