package com.middleearth.db;

import com.middleearth.engine.Region;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionRepository {
    public Region getById(int id) {
        String sql = "SELECT * FROM regions WHERE id = ?";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            System.err.println("[DB] Failed to get region: " + e.getMessage());
        }
        return null;
    }

    public List<Region> getAll() {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT * FROM regions ORDER BY id";
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                regions.add(mapRow(rs));
            }
        } catch (Exception e) {
            System.err.println("[DB] Failed to get regions: " + e.getMessage());
        }
        return regions;
    }

    private Region mapRow(ResultSet rs) throws SQLException {
        Region region = new Region(
                rs.getString("name"),
                rs.getString("description"),
                rs.getInt("xp"));
        region.setId(rs.getInt("id"));
        return region;
    }
}
