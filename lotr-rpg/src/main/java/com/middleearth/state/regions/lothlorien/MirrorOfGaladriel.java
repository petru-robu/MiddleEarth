package com.middleearth.state.regions.lothlorien;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.db.InventoryItemRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class MirrorOfGaladriel implements GameState {

    private final Quest quest;

    // Each entry: { narration, choice_a, choice_b, choice_c, correct_choice (1/2/3) }
    private static final String[][] VISIONS = {
        {
            "The silver water stirs. You see a vast Eye, lidless and wreathed in flame,\n" +
            "  turning slowly across a dark land. It searches — for something precious.\n" +
            "  What does this vision reveal?",
            "1) The fall of Númenor into the sea",
            "2) Sauron's search for the One Ring",
            "3) Gandalf's battle with the Balrog",
            "2"
        },
        {
            "A great tower rises from a rocky island where two rivers meet.\n" +
            "  A palantír sits at its peak. A white hand grips a staff nearby.\n" +
            "  What place does the mirror show?",
            "1) Minas Tirith, the White City",
            "2) Barad-dûr, the Dark Tower of Mordor",
            "3) Orthanc, the tower of Isengard",
            "3"
        },
        {
            "You see a halfling — barefoot, curly-haired — slipping a golden ring onto a finger\n" +
            "  and vanishing entirely from sight. He stands in a crowded hall.\n" +
            "  At what event does this happen?",
            "1) Bilbo's farewell party in the Shire",
            "2) The Council of Elrond in Rivendell",
            "3) The feast of King Théoden in Edoras",
            "1"
        },
        {
            "The mirror darkens. You see a great battle: the Last Alliance of Elves and Men.\n" +
            "  A dark lord strides across the field, casting warriors aside like leaves.\n" +
            "  Who finally cuts the Ring from the dark lord's hand?",
            "1) Elendil, High King of Gondor and Arnor",
            "2) Isildur, son of Elendil",
            "3) Gil-galad, the last High King of the Noldor",
            "2"
        }
    };

    private static final int VISIONS_TO_PASS = 3;

    public MirrorOfGaladriel(Quest quest) {
        this.quest = quest;
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();
        Player player = GameSession.getInstance().getPlayer();

        ui.clear();
        ui.renderFlashes();
        ui.renderTitle(quest.getTitle());
        ui.renderSubtitle(quest.getDescription());

        ui.render(Renderer.Style.CYAN +
            "\nGaladriel gestures to the silver basin. \"The mirror shows many things,\" she says,\n" +
            "\"things that were, things that are, and some things... that have not yet come to pass.\"\n\n" +
            "Interpret " + VISIONS_TO_PASS + " of " + VISIONS.length + " visions correctly to earn her trust.\n" +
            Renderer.Style.RESET);

        ui.prompt("Press ENTER to gaze into the Mirror...");

        int correct = 0;

        for (int i = 0; i < VISIONS.length; i++) {
            ui.clear();
            ui.renderTitle("Vision " + (i + 1) + " of " + VISIONS.length);

            String narration    = VISIONS[i][0];
            String choiceA      = VISIONS[i][1];
            String choiceB      = VISIONS[i][2];
            String choiceC      = VISIONS[i][3];
            String correctChoice = VISIONS[i][4];

            ui.render(Renderer.Style.CYAN + "\n" + narration + "\n" + Renderer.Style.RESET);
            ui.render("");
            ui.render("  " + choiceA);
            ui.render("  " + choiceB);
            ui.render("  " + choiceC);
            ui.render("");

            String answer = ui.prompt("Your answer (1/2/3)").trim();

            if (answer.equals(correctChoice)) {
                correct++;
                ui.render(Renderer.Style.GREEN + "\nGaladriel nods. \"You see truly.\" (" + correct + "/" + VISIONS_TO_PASS + " needed)" + Renderer.Style.RESET);
            } else {
                ui.render(Renderer.Style.RED + "\nThe vision fades. Galadriel's gaze is distant. The answer was: " + correctChoice + Renderer.Style.RESET);
            }

            ui.prompt("Press ENTER to continue...");

            if (correct >= VISIONS_TO_PASS) break;
        }

        ui.clear();

        if (correct >= VISIONS_TO_PASS) {
            ui.render(Renderer.Style.GREEN +
                "\nGaladriel smiles, a rare and luminous thing. \"You have seen what others miss.\n" +
                "The forest is safe — for now. Go with the light of Lórien, traveller.\"\n\n" +
                "You earned " + quest.getXpReward() + " XP!" +
                Renderer.Style.RESET);

            player.addXp(quest.getXpReward());

            if (quest.getItemReward() != null) {
                ui.render("You also received: " + Renderer.Style.GOLD + quest.getItemReward().getName() + Renderer.Style.RESET);
                new InventoryItemRepository().addItemToPlayer(player.getId(), quest.getItemReward().getId());
            }

            new CompletedQuestRepository().markCompleted(player.getId(), quest.getId());
            ui.addFlashInfo("Quest completed: " + quest.getTitle());
            ui.addFlashInfo("Got " + quest.getXpReward() + " XP!");
        } else {
            ui.render(Renderer.Style.RED +
                "\nThe Mirror clouds over. \"You look, but you do not see,\" Galadriel says quietly.\n" +
                "\"Return when your mind is clearer.\"\n" +
                Renderer.Style.RESET);
            ui.addFlashError("You failed to interpret the visions. Try again.");
        }

        ui.prompt("Press ENTER to return to Lothlórien...");
        return new Lothlorien();
    }
}
