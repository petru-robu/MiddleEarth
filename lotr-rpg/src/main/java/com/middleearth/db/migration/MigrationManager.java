package com.middleearth.db.migration;

import com.middleearth.db.DatabaseConfiguration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationManager {

    private static final String MIGRATION_TABLE = "schema_migrations";
    private static final String MIGRATION_PATH = "/db/migrations/";

    public void migrate() throws SQLException {
        ensureMigrationTable();
        List<Migration> pending = getPendingMigrations();

        if (pending.isEmpty()) {
            System.out.println("[Migration] Database is up to date.");
            return;
        }

        for (Migration m : pending) {
            System.out.println("[Migration] Applying V" + m.getVersion() + ": " + m.getDescription());
            executeMigration(m);
            recordMigration(m);
        }

        System.out.println("[Migration] All migrations applied successfully.");
    }

    private void ensureMigrationTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + MIGRATION_TABLE + " ("
                + "version INT PRIMARY KEY, "
                + "description VARCHAR(255) NOT NULL, "
                + "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        DatabaseConfiguration.execute(sql);
    }

    private List<Integer> getAppliedVersions() throws SQLException {
        List<Integer> versions = new ArrayList<>();
        String sql = "SELECT version FROM " + MIGRATION_TABLE;
        try (Statement stmt = DatabaseConfiguration.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                versions.add(rs.getInt("version"));
            }
        }
        return versions;
    }

    private List<Migration> getPendingMigrations() throws SQLException {
        List<Integer> applied = getAppliedVersions();
        List<Migration> allMigrations = discoverMigrations();

        return allMigrations.stream()
                .filter(m -> !applied.contains(m.getVersion()))
                .sorted(Comparator.comparingInt(Migration::getVersion))
                .collect(Collectors.toList());
    }

    private List<Migration> discoverMigrations() {
        List<Migration> migrations = new ArrayList<>();

        // Read the index file that lists all migration filenames
        InputStream indexStream = getClass().getResourceAsStream(MIGRATION_PATH + "migrations.index");
        if (indexStream == null) {
            System.err.println("[Migration] No migrations.index found in classpath.");
            return migrations;
        }

        try {
            List<String> filenames;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(indexStream, StandardCharsets.UTF_8))) {
                filenames = reader.lines()
                        .map(String::trim)
                        .filter(f -> f.endsWith(".sql"))
                        .sorted()
                        .collect(Collectors.toList());
            }

            for (String filename : filenames) {
                Migration m = parseMigrationFile(filename);
                if (m != null) {
                    migrations.add(m);
                }
            }
        } catch (Exception e) {
            System.err.println("[Migration] Error discovering migrations: " + e.getMessage());
        }

        return migrations;
    }

    private Migration parseMigrationFile(String filename) {
        // Expected format: V1__description.sql
        if (!filename.startsWith("V") || !filename.contains("__")) {
            return null;
        }

        try {
            String withoutExt = filename.replace(".sql", "");
            String[] parts = withoutExt.split("__", 2);
            int version = Integer.parseInt(parts[0].substring(1));
            String description = parts[1].replace("_", " ");

            InputStream is = getClass().getResourceAsStream(MIGRATION_PATH + filename);
            if (is == null) {
                return null;
            }

            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }

            return new Migration(version, description, sql);
        } catch (Exception e) {
            System.err.println("[Migration] Could not parse migration file: " + filename);
            return null;
        }
    }

    private void executeMigration(Migration migration) throws SQLException {
        Connection conn = DatabaseConfiguration.getConnection();
        // Split on semicolons to support multi-statement migrations
        String[] statements = migration.getSql().split(";");
        for (String s : statements) {
            String trimmed = s.trim();
            if (!trimmed.isEmpty()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    private void recordMigration(Migration migration) throws SQLException {
        String sql = "INSERT INTO " + MIGRATION_TABLE + " (version, description) VALUES (?, ?)";
        try (PreparedStatement ps = DatabaseConfiguration.getConnection().prepareStatement(sql)) {
            ps.setInt(1, migration.getVersion());
            ps.setString(2, migration.getDescription());
            ps.executeUpdate();
        }
    }
}
