package com.middleearth.engine;

import com.middleearth.items.Item;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<Item> items;
    private final double maxWeightCapacity;

    public Inventory(double maxWeightCapacity) {
        this.items = new ArrayList<>();
        this.maxWeightCapacity = maxWeightCapacity;
    }

    /**
     * Attempts to add an item. Fails if it exceeds weight capacity.
     */
    public boolean addItem(Item item) {
        if (getCurrentWeight() + item.getWeight() > maxWeightCapacity) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public List<Item> getAllItems() {
        return items;
    }

    public double getCurrentWeight() {
        double total = 0;
        for (Item item : items) {
            total += item.getWeight();
        }
        return total;
    }

    public double getMaxWeightCapacity() {
        return maxWeightCapacity;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}