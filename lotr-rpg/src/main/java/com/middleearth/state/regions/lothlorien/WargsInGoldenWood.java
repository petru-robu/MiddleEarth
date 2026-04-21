package com.middleearth.state.regions.lothlorien;

import com.middleearth.engine.BattleEngine;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class WargsInGoldenWood implements GameState {

    private final Quest quest;

    public WargsInGoldenWood(Quest quest) {
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
            "\nLow growls echo beneath the golden mallorn canopy. A warg pack closes in.\n" +
            "You are alone in the wood — stand your ground!\n" +
            Renderer.Style.RESET);
        ui.prompt("Press ENTER to engage...");
        return BattleEngine.run(quest, new Lothlorien());
    }
}
