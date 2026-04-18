package com.middleearth.db;

import com.middleearth.engine.CharacterClass;
import com.middleearth.engine.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository implements GenericRepository<Player> {

    @Override
    public void insert(Player player) {
        String sql = "INSERT INTO players (name, class_id, health, xp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, player.getName());
            ps.setInt(2, player.getCharacterClass() != null ? player.getCharacterClass().getId() : 0);
            ps.setInt(3, player.getHealth());
            ps.setInt(4, player.getXp());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    player.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to insert player: " + e.getMessage());
        }
    }

    @Override
    public Player getById(int id) {
        String sql = "SELECT * FROM players WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get player: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM players";
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                players.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get players: " + e.getMessage());
        }
        return players;
    }

    @Override
    public void update(Player player) {
        String sql = "UPDATE players SET name = ?, class_id = ?, health = ?, xp = ? WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setString(1, player.getName());
            ps.setInt(2, player.getCharacterClass() != null ? player.getCharacterClass().getId() : 0);
            ps.setInt(3, player.getHealth());
            ps.setInt(4, player.getXp());
            ps.setInt(5, player.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to update player: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM players WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[DB] Failed to delete player: " + e.getMessage());
        }
    }

    private final CharacterClassRepository classRepo = new CharacterClassRepository();

    private Player mapRow(ResultSet rs) throws SQLException {
        int classId = rs.getInt("class_id");
        CharacterClass cc = classId > 0 ? classRepo.getById(classId) : null;

        Player p;
        if (cc != null) {
            p = new Player(rs.getString("name"), cc);
        } else {
            p = new Player(rs.getString("name"));
        }
        p.setId(rs.getInt("id"));
        p.setHealth(rs.getInt("health"));
        p.addXp(rs.getInt("xp"));
        return p;
    }
}
