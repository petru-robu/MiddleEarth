package com.middleearth.state.regions.lothlorien;

import com.middleearth.engine.BattleEngine;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class OrcRaid implements GameState {

    private final Quest quest;

    public OrcRaid(Quest quest) {
        this.quest = quest;
    }

    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();
        ui.clear();
        ui.renderFlashes();
        ui.renderTitle(quest.getTitle());
        ui.renderSubtitle(quest.getDescription());
        ui.render(Renderer.Style.RED +
            "\nThe trees are alive with orc war-cries! The city of Caras Galadhon is under attack!\n" +
            "Drive them back before they reach the heart of Lórien!\n" +
            Renderer.Style.RESET);
        ui.prompt("Press ENTER to engage...");
        return BattleEngine.run(quest, new Lothlorien());
    }
}
