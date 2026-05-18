package com.middleearth.state;

public interface GameState {
    /*
        Interface for GameState.
        Derived classes are forced to implement update() which returns next GameState.
    */
    GameState update();
}