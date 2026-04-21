package com.middleearth.state.regions.blackgate;

import com.middleearth.engine.BattleEngine;
import com.middleearth.engine.Quest;
import com.middleearth.state.GameState;
import com.middleearth.ui.Renderer;

public class OlogHaiVanguard implements GameState {

    private final Quest quest;

    public OlogHaiVanguard(Quest quest) {
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
            "\nThe ground shakes beneath armoured feet. Olog-hai — Sauron's great war-trolls —\n" +
            "lead the vanguard. They cannot be reasoned with. They must be stopped.\n" +
            Renderer.Style.RESET);
        ui.prompt("Press ENTER to engage...");
        return BattleEngine.run(quest, new Blackgate());
    }
}
