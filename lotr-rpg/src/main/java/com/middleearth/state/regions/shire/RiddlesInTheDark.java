package com.middleearth.state.regions.shire;

import com.middleearth.db.CompletedQuestRepository;
import com.middleearth.engine.GameSession;
import com.middleearth.engine.Player;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class RiddlesInTheDark implements GameState{

    private final Quest quest;

    public RiddlesInTheDark(Quest quest) {
        this.quest = quest;
    }
    
    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle(quest.getTitle());

        ui.renderSubtitle(quest.getDescription());

        ui.render("\n[Placeholder for puzzle mechanics]\n");
        ui.prompt("Something");

        ui.render("Congratulations! You've completed the quest and earned " + quest.getXpReward() + " XP!");
        
        Player player = GameSession.getInstance().getPlayer();
        player.addXp(quest.getXpReward());

        if (quest.getItemReward() != null) {
            ui.render("You've also received: " + quest.getItemReward().getName());
        }

        new CompletedQuestRepository().markCompleted(player.getId(), quest.getId());
        ui.addFlashInfo("Quest completed: " + quest.getTitle());
        ui.addFlashInfo("Got " + quest.getXpReward() + " XP!");

        return new Shire(); 
    }
}
