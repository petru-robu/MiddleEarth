package com.middleearth.db;

import com.middleearth.engine.Enemy;
import com.middleearth.engine.Enemy.EnemyType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyRepository {

    private final ItemRepository itemRepo = new ItemRepository();

    public List<Enemy> getByQuestId(int questId) {
        List<Enemy> enemies = new ArrayList<>();
        String sql = "SELECT * FROM enemies WHERE quest_id = ? ORDER BY id";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, questId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enemies.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to load enemies: " + e.getMessage());
        }
        return enemies;
    }

    private Enemy mapRow(ResultSet rs) throws SQLException {
        Enemy enemy = new Enemy(
            rs.getString("name"),
            EnemyType.valueOf(rs.getString("type")),
            rs.getInt("health"),
            rs.getInt("attack")
        );
        enemy.setId(rs.getInt("id"));
        enemy.setQuestId(rs.getInt("quest_id"));

        int lootItemId = rs.getInt("loot_item_id");
        if (!rs.wasNull() && lootItemId > 0) {
            enemy.setLootItem(itemRepo.getById(lootItemId));
        }

        enemy.setAsciiArt(rs.getString("ascii_art"));

        return enemy;
    }
}
