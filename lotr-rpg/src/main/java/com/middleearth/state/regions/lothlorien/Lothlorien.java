package com.middleearth.state.regions.lothlorien;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.db.QuestRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Quest;
import com.middleearth.ui.Renderer;
import com.middleearth.state.GameState;
import com.middleearth.ui.CommandInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lothlorien implements GameState {

    private static final int REGION_ID = 2;

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        ui.clear();
        ui.renderFlashes();
        ui.renderTitle("Welcome to Lothlórien");

        ui.renderSubtitle(
                "The golden forest of the Galadhrim. Enchanted, beautiful, and dangerous.");

        ui.render("\nAvailable Quests:");

        QuestRepository questRepo = new QuestRepository();
        List<Quest> quests = questRepo.getByRegionId(REGION_ID);

        int playerId = GameSession.getInstance().getPlayer().getId();
        CompletedQuestRepository completedRepo = new CompletedQuestRepository();
        Map<Integer, LocalDateTime> completedIds = completedRepo.getCompletedQuestIds(playerId);

        List<String> options = new ArrayList<>();
        for (Quest q : quests) {
            boolean done = completedIds.containsKey(q.getId());
            if (done) {
                LocalDateTime ts = completedIds.get(q.getId());
                String when = ts != null ? ts.format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")) : "";
                options.add(Renderer.Style.GREEN + q.getTitle() + " [Completed at " + when + "]" + Renderer.Style.RESET + "\n");
            } else {
                String tag = q.getQuestType() == Quest.QuestType.BATTLE
                        ? Renderer.Style.RED + "[Battle]" + Renderer.Style.RESET
                        : Renderer.Style.CYAN + "[Puzzle]" + Renderer.Style.RESET;
                options.add(tag + " " + Renderer.Style.GOLD + q.getTitle() + Renderer.Style.RESET
                        + "\n      " + Renderer.Style.GRAY + q.getDescription() + Renderer.Style.RESET
                        + "\n      Difficulty: " + q.getDifficulty() + "  |  XP: " + q.getXpReward()
                        + (q.getItemReward() != null ? "  |  Reward: " + q.getItemReward().getName() : "")
                        + "\n");
            }
        }
        options.add("Return to Open World");

        ui.renderOptions(options);
        String input = ui.prompt("Choose a quest");

        if (input.startsWith(":")) {
            return CommandInterceptor.handle(input, this);
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice == options.size()) {
                return new com.middleearth.state.game.OpenWorld(this);
            }
            if (choice >= 1 && choice <= quests.size()) {
                Quest selected = quests.get(choice - 1);
                if (completedIds.containsKey(selected.getId())) {
                    ui.addFlashError("You have already completed this quest.");
                    return this;
                }
                int questIndex = quests.indexOf(selected) + 1;
                switch (questIndex) {
                    case 1: return new MirrorOfGaladriel(selected);
                    case 2: return new OrcRaid(selected);
                    case 3: return new WargsInGoldenWood(selected);
                    default:
                        ui.addFlashError("This quest is not yet available.");
                        return this;
                }
            }
        } catch (NumberFormatException ignored) {}

        ui.addFlashError("Invalid choice.");
        return this;
    }
}
