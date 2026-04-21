package com.middleearth.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton service that appends an action to audit.csv
 * CSV: action_name,timestamp
 */
public class AuditService {

    private static final String FILE_PATH = "audit.csv";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static AuditService instance;

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String action) {
        String timestamp = LocalDateTime.now().format(FMT);
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            pw.println(action + "," + timestamp);
        } catch (IOException e) {
            // Non-fatal: audit failure must never crash the game
            System.err.println("[AuditService] Failed to write audit log: " + e.getMessage());
        }
    }
    
    public static final String ATTACK            = "ATTACK";
    public static final String USE_HEALING_ITEM  = "USE_HEALING_ITEM";
    public static final String FLEE_BATTLE       = "FLEE_BATTLE";
    public static final String EQUIP_WEAPON      = "EQUIP_WEAPON";
    public static final String EQUIP_ARMOR       = "EQUIP_ARMOR";
    public static final String UNEQUIP_WEAPON    = "UNEQUIP_WEAPON";
    public static final String UNEQUIP_ARMOR     = "UNEQUIP_ARMOR";
    public static final String CONSUME_ITEM      = "CONSUME_ITEM";
    public static final String START_QUEST       = "START_QUEST";
    public static final String COMPLETE_QUEST    = "COMPLETE_QUEST";
    public static final String TRAVEL_TO_REGION  = "TRAVEL_TO_REGION";
    public static final String LOOT_ITEM         = "LOOT_ITEM";
    public static final String VIEW_INVENTORY    = "VIEW_INVENTORY";
    public static final String NEW_GAME          = "NEW_GAME";
    public static final String QUIT_GAME         = "QUIT_GAME";
}
