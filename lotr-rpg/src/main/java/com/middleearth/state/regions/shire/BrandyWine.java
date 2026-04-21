package com.middleearth.state.regions.shire;

import com.middleearth.engine.BattleEngine;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class BrandyWine implements GameState {

    private final Quest quest;

    public BrandyWine(Quest quest) {
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
            "\nA band of ruffians blocks Bucklebury Ferry. Steady your nerves — drive them off!\n" +
            Renderer.Style.RESET);
        ui.prompt("Press ENTER to engage...");
        return BattleEngine.run(quest, new Shire());
    }
}

