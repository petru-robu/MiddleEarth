package com.middleearth.db;

import com.middleearth.engine.CharacterClass;
import com.middleearth.items.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CharacterClassRepository implements GenericRepository<CharacterClass> {

    private final ItemRepository itemRepo = new ItemRepository();

    @Override
    public void insert(CharacterClass obj) {
        throw new UnsupportedOperationException("Classes are seeded via migrations.");
    }

    @Override
    public CharacterClass getById(int id) {
        String sql = "SELECT * FROM character_classes WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get class: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<CharacterClass> getAll() {
        List<CharacterClass> classes = new ArrayList<>();
        String sql = "SELECT * FROM character_classes ORDER BY id";
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                classes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("[DB] Failed to get classes: " + e.getMessage());
        }
        return classes;
    }

    @Override
    public void update(CharacterClass obj) {
        throw new UnsupportedOperationException("Classes are seeded via migrations.");
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException("Classes are seeded via migrations.");
    }

    private CharacterClass mapRow(ResultSet rs) throws SQLException {
        Item starterWeapon = itemRepo.getById(rs.getInt("starter_weapon_id"));
        Item starterItem = itemRepo.getById(rs.getInt("starter_item_id"));

        CharacterClass cc = new CharacterClass(
            rs.getString("name"),
            rs.getString("description"),
            rs.getInt("base_health"),
            rs.getDouble("bag_capacity"),
            starterWeapon,
            starterItem
        );
        cc.setId(rs.getInt("id"));
        return cc;
    }
}
