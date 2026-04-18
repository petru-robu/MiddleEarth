package com.middleearth.db;

import com.middleearth.engine.Quest;
import com.middleearth.engine.Quest.QuestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestRepository implements GenericRepository<Quest> {

    private final ItemRepository itemRepo = new ItemRepository();

    @Override
    public void insert(Quest obj) {
        throw new UnsupportedOperationException("Quests are seeded via migrations.");
    }

    @Override
    public Quest getById(int id) {
        String sql = "SELECT * FROM quests WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get quest: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Quest> getAll() {
        List<Quest> quests = new ArrayList<>();
        String sql = "SELECT * FROM quests ORDER BY id";
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                quests.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get quests: " + e.getMessage());
        }
        return quests;
    }

    public List<Quest> getByRegionId(int regionId) {
        List<Quest> quests = new ArrayList<>();
        String sql = "SELECT * FROM quests WHERE region_id = ? ORDER BY id";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, regionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quests.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get quests for region: " + e.getMessage());
        }
        return quests;
    }

    @Override
    public void update(Quest obj) {
        throw new UnsupportedOperationException("Quests are seeded via migrations.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Quests are seeded via migrations.");
    }

    private Quest mapRow(ResultSet rs) throws SQLException {
        Quest quest = new Quest(
            rs.getString("title"),
            rs.getString("description"),
            QuestType.valueOf(rs.getString("quest_type")),
            rs.getInt("difficulty"),
            rs.getInt("xp_reward")
        );
        quest.setId(rs.getInt("id"));
        quest.setRegionId(rs.getInt("region_id"));

        int itemRewardId = rs.getInt("item_reward_id");
        if (!rs.wasNull() && itemRewardId > 0) {
            quest.setItemReward(itemRepo.getById(itemRewardId));
        }

        return quest;
    }
}
