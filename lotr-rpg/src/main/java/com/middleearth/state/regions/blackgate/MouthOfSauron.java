package com.middleearth.state.regions.blackgate;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.db.InventoryItemRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

import java.util.Random;

public class MouthOfSauron implements GameState {

    private final Quest quest;
    private final Random rand = new Random();

    // Each entry: { setup_text, claim_a, claim_b, claim_c, claim_d, index_of_lie (0-based) }
    // The Mouth presents 3 truths and 1 lie — player must spot which is the lie.
    private static final String[][] ROUNDS = {
        {
            "The Mouth of Sauron smiles his lipless smile. \"Let us play a game of truth, little warrior.\n" +
            "  I shall speak four claims about the war. One — and only one — is a lie.\n" +
            "  Find it, and I shall let you pass. Fail, and your allies suffer.\"",
            "A) The One Ring was forged in the fires of Mount Doom",
            "B) Sauron was once a Maia of Aulë the Smith",
            "C) The Nazgûl are the nine kings of Men who took rings of power",
            "D) The palantír of Orthanc was destroyed when Isengard fell",
            "3"   // D is the lie — the palantír survived (Pippin looked into it)
        },
        {
            "\"Impressive. You are sharper than you look.\" His smile does not reach his eyes.\n" +
            "  \"Another round, then. Four facts of Middle-earth. One is a fabrication.\"",
            "A) Shelob is a great spider, last child of Ungoliant",
            "B) The Dead Marshes hide the faces of those slain in the Battle of Dagorlad",
            "C) Minas Morgul was once called Minas Anor, the Tower of the Sun",
            "D) Cirith Ungol means 'Pass of the Spider' in the Black Speech",
            "2"   // C is the lie — Minas Morgul was Minas Ithil (Tower of the Moon); Minas Anor became Minas Tirith
        },
        {
            "\"You persist.\" A flicker of something crosses his ruined face.\n" +
            "  \"One final gambit. Four statements. Unmask the deception.\"",
            "A) Anduril was reforged from the shards of Narsil",
            "B) Glamdring, the sword of Gandalf, was once an elvish blade called 'Foe-hammer'",
            "C) Sting glows blue in the presence of orcs because it was made in Gondolin",
            "D) The Balrog Durin's Bane had dwelt in Khazad-dûm since the Second Age",
            "3"   // D is the lie — Durin's Bane was awoken in the Third Age when dwarves dug too deep
        }
    };

    private static final int ROUNDS_TO_PASS = 2;

    public MouthOfSauron(Quest quest) {
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

        ui.render(Renderer.Style.RED +
            "\nA figure emerges from the Black Gate — helmed, armoured, and smiling a smile\n" +
            "that has no warmth in it. The Mouth of Sauron. His master's herald.\n\n" +
            "He challenges you to a game of wits. Outsmart him, or your allies pay the price.\n" +
            Renderer.Style.RESET);

        ui.prompt("Press ENTER to face the Mouth of Sauron...");

        int correct = 0;

        for (int i = 0; i < ROUNDS.length; i++) {
            ui.clear();
            ui.renderTitle("Round " + (i + 1) + " of " + ROUNDS.length);

            String setup      = ROUNDS[i][0];
            String claimA     = ROUNDS[i][1];
            String claimB     = ROUNDS[i][2];
            String claimC     = ROUNDS[i][3];
            String claimD     = ROUNDS[i][4];
            String lieIndex   = ROUNDS[i][5];

            ui.render(Renderer.Style.GOLD + "\n" + setup + "\n" + Renderer.Style.RESET);
            ui.render("");
            ui.render("  " + claimA);
            ui.render("  " + claimB);
            ui.render("  " + claimC);
            ui.render("  " + claimD);
            ui.render("");

            String answer = ui.prompt("Which is the lie? (A/B/C/D)").trim().toUpperCase();
            String expectedLetter = getLetter(lieIndex);

            if (answer.equals(expectedLetter)) {
                correct++;
                ui.render(Renderer.Style.GREEN +
                    "\nThe Mouth of Sauron's smile falters. \"... Lucky guess.\"\n" +
                    "(" + correct + "/" + ROUNDS_TO_PASS + " needed)" + Renderer.Style.RESET);
            } else {
                ui.render(Renderer.Style.RED +
                    "\nHe laughs — a sound like grinding iron. \"Wrong! The lie was " + expectedLetter + ".\"" +
                    Renderer.Style.RESET);
            }

            ui.prompt("Press ENTER to continue...");

            if (correct >= ROUNDS_TO_PASS) break;
        }

        ui.clear();

        if (correct >= ROUNDS_TO_PASS) {
            ui.render(Renderer.Style.GREEN +
                "\nThe Mouth of Sauron stares at you for a long moment, then wheels his horse\n" +
                "and retreats behind the gate. You have outwitted the herald of the Dark Lord.\n\n" +
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
                "\n\"Pathetic.\" The Mouth of Sauron sneers and turns away.\n" +
                "\"My master grows impatient. We shall speak again when you are worthy.\"\n" +
                Renderer.Style.RESET);
            ui.addFlashError("You were outwitted by the Mouth of Sauron. Try again.");
        }

        ui.prompt("Press ENTER to return to the Black Gate...");
        return new Blackgate();
    }

    private static String getLetter(String zeroBasedIndex) {
        switch (zeroBasedIndex) {
            case "0": return "A";
            case "1": return "B";
            case "2": return "C";
            case "3": return "D";
            default:  return "A";
        }
    }
}
