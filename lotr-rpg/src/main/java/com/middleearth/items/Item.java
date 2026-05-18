package com.middleearth.items;

public interface Item {
    /* 
        Item interface:
        - id
        - name
        - description
    */

    int getId();

    String getName();

    String getDescription();

    double getWeight();
}