package com.middleearth.db;

import com.middleearth.items.EquipmentSlot;
import com.middleearth.items.Item;
import com.middleearth.items.types.Armor;
import com.middleearth.items.types.Potion;
import com.middleearth.items.types.Weapon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public Item getById(int id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get item: " + e.getMessage());
        }
        return null;
    }

    public Item getByName(String name) {
        String sql = "SELECT * FROM items WHERE name = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get item by name: " + e.getMessage());
        }
        return null;
    }

    public List<Item> getAll() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY id";
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                items.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get items: " + e.getMessage());
        }
        return items;
    }

    static Item mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String type = rs.getString("item_type");
        String description = rs.getString("description");
        double weight = rs.getDouble("weight");
        int bonus = rs.getInt("bonus");
        String slotStr = rs.getString("slot");
        int restoreAmount = rs.getInt("restore_amount");

        switch (type) {
            case "WEAPON":
                Weapon w = new Weapon(name, description, weight, bonus, false);
                w.setId(id);
                return w;
            case "ARMOR":
                EquipmentSlot armorSlot = EquipmentSlot.CHEST;
                if (slotStr != null) {
                    try {
                        armorSlot = EquipmentSlot.valueOf(slotStr);
                    } catch (IllegalArgumentException ignored) {}
                }
                Armor a = new Armor(name, description, weight, armorSlot, bonus);
                a.setId(id);
                return a;
            case "POTION":
                Potion p = new Potion(name, description, weight, restoreAmount, "You use the " + name + ".");
                p.setId(id);
                return p;
            default:
                System.err.println("[DB] Unknown item type: " + type);
                return null;
        }
    }
}
