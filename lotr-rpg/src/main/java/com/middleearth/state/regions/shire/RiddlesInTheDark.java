package com.middleearth.state.regions.shire;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class RiddlesInTheDark implements GameState {

    private final Quest quest;

    // Each entry: { question, accepted answer(s) separated by | }
    private static final String[][] RIDDLES = {
        {
            "Gollum speaks: \"It cannot be seen, cannot be felt,\n" +
            "  Cannot be heard, cannot be smelt.\n" +
            "  It lies behind stars and under hills,\n" +
            "  And empty holes it fills.\n" +
            "  It comes first and follows after,\n" +
            "  Ends life, kills laughter.\"\n" +
            "  What is it?",
            "dark|darkness"
        },
        {
            "What is the elvish word for 'friend' that opens the Doors of Durin?",
            "mellon"
        },
        {
            "What is the name of Bilbo Baggins' home in the Shire?",
            "bag end"
        },
        {
            "Reforged from the shards of Narsil, what is the name of Aragorn's sword?",
            "anduril|andúril"
        },
        {
            "What creature was Gollum before the Ring corrupted him?",
            "hobbit|stoor|stoor hobbit"
        }
    };

    private static final int RIDDLES_TO_PASS = 3;

    public RiddlesInTheDark(Quest quest) {
        this.quest = quest;
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        ui.clear();
        ui.renderFlashes();
        ui.renderTitle(quest.getTitle());
        ui.renderSubtitle(quest.getDescription());

        ui.render(Renderer.Style.GOLD +
            "\nGollum has challenged you to a game of riddles in the dark!\n" +
            "Answer " + RIDDLES_TO_PASS + " out of " + RIDDLES.length + " correctly to claim your reward.\n" +
            Renderer.Style.RESET);

        ui.prompt("Press ENTER to begin...");

        int correct = 0;

        for (int i = 0; i < RIDDLES.length; i++) {
            ui.clear();
            ui.renderTitle("Riddle " + (i + 1) + " of " + RIDDLES.length);

            String question = RIDDLES[i][0];
            String[] acceptedAnswers = RIDDLES[i][1].split("\\|");

            ui.render(Renderer.Style.CYAN + "\n" + question + "\n" + Renderer.Style.RESET);

            String answer = ui.prompt("Your answer").trim().toLowerCase();

            boolean isCorrect = false;
            for (String accepted : acceptedAnswers) {
                if (answer.equals(accepted.trim().toLowerCase())) {
                    isCorrect = true;
                    break;
                }
            }

            if (isCorrect) {
                correct++;
                ui.render(Renderer.Style.GREEN + "\nCorrect! (" + correct + "/" + RIDDLES_TO_PASS + " needed)" + Renderer.Style.RESET);
            } else {
                ui.render(Renderer.Style.RED + "\nWrong! Gollum hisses: \"Thief! Baggins!\" The answer was: " + acceptedAnswers[0] + Renderer.Style.RESET);
            }

            ui.prompt("Press ENTER to continue...");

            if (correct >= RIDDLES_TO_PASS) {
                break;
            }
        }

        ui.clear();
        Player player = GameSession.getInstance().getPlayer();

        if (correct >= RIDDLES_TO_PASS) {
            ui.render(Renderer.Style.GREEN +
                "\nGollum slinks away into the dark, furious but defeated.\n" +
                "You have won the riddle game!\n" +
                "Congratulations! You earned " + quest.getXpReward() + " XP!" +
                Renderer.Style.RESET);

            player.addXp(quest.getXpReward());

            if (quest.getItemReward() != null) {
                ui.render("You also received: " + quest.getItemReward().getName());
            }

            new CompletedQuestRepository().markCompleted(player.getId(), quest.getId());
            ui.addFlashInfo("Quest completed: " + quest.getTitle());
            ui.addFlashInfo("Got " + quest.getXpReward() + " XP!");
        } else {
            ui.render(Renderer.Style.RED +
                "\nGollum cackles: \"Stupid, fat hobbit! We wins! We wins!\"\n" +
                "You failed the riddle game. Only " + correct + "/" + RIDDLES_TO_PASS + " correct.\n" +
                "Return and try again..." +
                Renderer.Style.RESET);
            ui.addFlashError("You failed the riddle game! Try again.");
        }

        ui.prompt("Press ENTER to return to the Shire...");
        return new Shire();
    }
}
