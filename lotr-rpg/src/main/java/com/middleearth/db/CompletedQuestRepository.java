package com.middleearth.db;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CompletedQuestRepository {

    public void markCompleted(int playerId, int questId) {
        String sql = "INSERT IGNORE INTO completed_quests (player_id, quest_id) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, questId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to mark quest completed: " + e.getMessage());
        }
    }

    public boolean isCompleted(int playerId, int questId) {
        String sql = "SELECT 1 FROM completed_quests WHERE player_id = ? AND quest_id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            ps.setInt(2, questId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to check quest completion: " + e.getMessage());
        }
        return false;
    }

    public Map<Integer, LocalDateTime> getCompletedQuestIds(int playerId) {
        Map<Integer, LocalDateTime> map = new HashMap<>();
        String sql = "SELECT quest_id, completed_at FROM completed_quests WHERE player_id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, playerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("completed_at");
                    map.put(rs.getInt("quest_id"), ts != null ? ts.toLocalDateTime() : null);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get completed quests: " + e.getMessage());
        }
        return map;
    }
}
