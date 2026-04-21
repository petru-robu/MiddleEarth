package com.middleearth.state.regions.blackgate;

import com.middleearth.engine.BattleEngine;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class SiegeOfMorannon implements GameState {

    private final Quest quest;

    public SiegeOfMorannon(Quest quest) {
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
            "\nThe Black Gate swings open. Legions of Mordor pour forth like a dark tide.\n" +
            "The fate of Middle-earth hangs on this moment. HOLD THE LINE!\n" +
            Renderer.Style.RESET);
        ui.prompt("Press ENTER to engage...");
        return BattleEngine.run(quest, new Blackgate());
    }
}
