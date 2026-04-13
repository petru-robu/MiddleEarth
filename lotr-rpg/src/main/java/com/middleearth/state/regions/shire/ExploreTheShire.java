package com.middleearth.state.regions.shire;

import com.middleearth.CommandInterceptor;
import com.middleearth.Renderer;
import com.middleearth.engine.Area;
import com.middleearth.engine.GameSession;
import com.middleearth.state.GameState;
import com.middleearth.engine.Player;

import java.util.ArrayList;
import java.util.List;

public class ExploreTheShire implements GameState{
    @Override
    public GameState update() {
        Renderer ui = Renderer.getInstance();

        // standard UI refresh
        ui.clear();
        ui.renderFlashes();

        ui.renderTitle("Exploring The Shire");

        return this;
    }
}
