package com.middleearth.state.regions.shire;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CropRaider implements GameState {

    private final Quest quest;

    private static final String[] FIELD_POOL = {
        "Bag Shot Row", "Bywater", "Hobbiton", "Overhill",
        "Tuckborough", "Whitfurrows", "Woodhall", "Stock"
    };

    public CropRaider(Quest quest) {
        this.quest = quest;
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();
        Random rand = new Random();

        // 1. Setup the Fields
        List<String> fieldPool = new ArrayList<>(Arrays.asList(FIELD_POOL));
        Collections.shuffle(fieldPool, rand);
        List<String> activeFields = new ArrayList<>(fieldPool.subList(0, 4));

        // 2. Setup the Logic Puzzle Roles
        // We shuffle the active fields to assign them arbitrary roles in our logic matrix.
        List<String> roles = new ArrayList<>(activeFields);
        Collections.shuffle(roles, rand);
        String roleA = roles.get(0); // The field two hobbits will argue about
        String roleB = roles.get(1); // Claimed hiding spot by the third hobbit
        String roleC = roles.get(2); // Claimed NOT to be the hiding spot by the fourth hobbit
        String roleD = roles.get(3); // The decoy field (never mentioned)

        // 3. Determine the Game Mode
        // True = 1 Truth-Teller (Target is C). False = 1 Liar (Target is B).
        boolean isOneTruth = rand.nextBoolean();
        String targetField = isOneTruth ? roleC : roleB;
        int raiderIndex = activeFields.indexOf(targetField);

        ui.clear();
        ui.renderFlashes();
        ui.renderTitle(quest.getTitle());
        ui.renderSubtitle(quest.getDescription());

        ui.render(Renderer.Style.GOLD +
            "\nFarmer Maggot bursts in: \"Someone's been at me mushrooms!\"\n" +
            "Four hobbits saw the raider flee, but they're all arguing over where he went.\n" +
            "Maggot whispers: \"Listen closely... I know these lads.\n" +
            (isOneTruth ? "Exactly ONE of them is telling the TRUTH!\"" : "Exactly ONE of them is LYING!\"") + "\n" +
            "Use your wits to deduce the raider's true hiding spot.\n" +
            Renderer.Style.RESET);

        ui.prompt("Press ENTER to interview the witnesses...");

        // 4. Build the Statements
        String[] hobbitNames = {"Sam", "Pippin", "Merry", "Frodo"};
        List<String> hobbits = Arrays.asList(hobbitNames);
        Collections.shuffle(hobbits, rand);

        List<String> clues = new ArrayList<>();
        clues.add(hobbits.get(0) + " swears: \"The raider is hiding in " + roleA + "!\"");
        clues.add(hobbits.get(1) + " argues: \"That's false! The raider is NOT in " + roleA + "!\"");
        clues.add(hobbits.get(2) + " claims: \"I saw the raider run into " + roleB + "!\"");
        clues.add(hobbits.get(3) + " states: \"I only know the raider is definitely NOT in " + roleC + ".\"");
        Collections.shuffle(clues, rand);

        // 5. Render the Puzzle
        ui.clear();
        ui.renderTitle("The Interrogation");
        ui.render(Renderer.Style.CYAN + "Rule: Exactly ONE hobbit is " + (isOneTruth ? "telling the TRUTH." : "LYING.") + Renderer.Style.RESET);

        ui.render(Renderer.Style.CYAN + "\nThe Suspect Fields:\n" + Renderer.Style.RESET);
        for (int i = 0; i < activeFields.size(); i++) {
            ui.render("  " + Renderer.Style.GOLD + (i + 1) + ") " + Renderer.Style.RESET + activeFields.get(i));
        }

        ui.render(Renderer.Style.CYAN + "\nWitness Statements:\n" + Renderer.Style.RESET);
        for (String clue : clues) {
            ui.render(Renderer.Style.GRAY + "  * " + clue + Renderer.Style.RESET);
        }

        // 6. Player Resolution Phase
        boolean success = false;
        int attempts = 0;
        while (attempts < 2) {
            ui.render("");
            String input = ui.prompt("Deduce the field! Which one is the raider in? [1-4]  (Attempt " + (attempts + 1) + "/2)").trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ui.render(Renderer.Style.RED + "Enter a number between 1 and 4." + Renderer.Style.RESET);
                continue;
            }

            if (choice < 1 || choice > 4) {
                ui.render(Renderer.Style.RED + "Enter a number between 1 and 4." + Renderer.Style.RESET);
                continue;
            }

            attempts++;

            if (choice - 1 == raiderIndex) {
                success = true;
                break;
            } else if (attempts < 2) {
                ui.render(Renderer.Style.RED + "You searched the field, but it's empty. Review the logic carefully..." + Renderer.Style.RESET);
            }
        }

        ui.clear();
        Player player = GameSession.getInstance().getPlayer();

        if (success) {
            ui.render(Renderer.Style.GREEN +
                "\nBrilliant deduction! You corner the raider in " + targetField + "!\n" +
                "Farmer Maggot claps you on the back:\n" +
                "\"You've got a sharp mind! Have some of me finest mushrooms!\"\n" +
                "You earned " + quest.getXpReward() + " XP!" +
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
                "\nThe logic failed you! The raider was hiding in " + targetField + " all along!\n" +
                "They vanish into the night, mushrooms and all.\n" +
                "Farmer Maggot shakes his fist: \"I should have brought the dogs!\"\n" +
                Renderer.Style.RESET);
            ui.addFlashError("The raider escaped! Try again.");
        }

        ui.prompt("Press ENTER to return to the Shire...");
        return new Shire();
    }
}