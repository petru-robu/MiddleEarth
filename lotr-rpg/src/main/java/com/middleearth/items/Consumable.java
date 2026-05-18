package com.middleearth.items;

public interface Consumable {
    /*
        Consumable items, must restore and return a use message.
    */
    int getRestoreAmount();
    
    String getUseMessage();
}
