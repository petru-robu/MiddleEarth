package com.middleearth.db;

import com.middleearth.items.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemRepository {

    public void addItemToPlayer(int playerId, int itemId) {
        String sql = "INSERT INTO inventory_items (player_id, item_id) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to add inventory item: " + e.getMessage());
        }
    }

    public List<Item> getItemsByPlayerId(int playerId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.* FROM items i JOIN inventory_items ii ON i.id = ii.item_id WHERE ii.player_id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = ItemRepository.mapRow(rs);
                    if (item != null) {
                        items.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to load inventory items: " + e.getMessage());
        }
        return items;
    }

    public double getTotalWeight(int playerId) {
        String sql = "SELECT COALESCE(SUM(i.weight), 0) FROM items i JOIN inventory_items ii ON i.id = ii.item_id WHERE ii.player_id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get inventory weight: " + e.getMessage());
        }
        return 0;
    }

    public void removeItemFromPlayer(int playerId, int itemId) {
        String sql = "DELETE FROM inventory_items WHERE player_id = ? AND item_id = ? LIMIT 1";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to remove inventory item: " + e.getMessage());
        }
    }

    public void deleteAllForPlayer(int playerId) {
        String sql = "DELETE FROM inventory_items WHERE player_id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to delete inventory items: " + e.getMessage());
        }
    }
}
