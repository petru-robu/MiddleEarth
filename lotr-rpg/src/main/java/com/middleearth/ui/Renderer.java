package com.middleearth.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.middleearth.engine.Region;

public class Renderer {

    private static Renderer instance;

    public static Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    public enum Style {
        RESET("\u001B[0m"),
        GOLD("\u001B[33m"),
        CYAN("\u001B[36m"),
        GREEN("\u001B[32m"),
        GRAY("\u001B[90m"),
        RED("\u001B[31m"),
        BOLD("\u001B[1m"),
        STRIKETHROUGH("\u001B[9m"),
        MAGENTA("\u001B[35m"),
        ORANGE("\u001B[38;5;208m"),
        ELECTRIC_BLUE("\u001B[94m");

        private final String code;

        Style(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    private final Scanner scanner = new Scanner(System.in);

    private Renderer() {
    }

    /**
     * Renders text. If no styles are provided, defaults to GRAY.
     * Otherwise, applies all provided styles (colors, bold, strikethrough, etc.)
     */
    public void render(String text, Style... styles) {
        if (styles.length == 0) {
            System.out.println(Style.GRAY + text + Style.RESET);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Style style : styles) {
            if (style != null) {
                sb.append(style.getCode());
            }
        }

        sb.append(text).append(Style.RESET);
        System.out.println(sb);
    }

    // --- Semantic Helpers ---

    public void renderTitle(String text) {
        String border = "=".repeat(text.length() + 8);
        System.out.println();
        render(border, Style.GOLD);
        render("=== " + text.toUpperCase() + " ===", Style.GOLD, Style.BOLD);
        render(border, Style.GOLD);
    }

    public void renderSubtitle(String text) {
        System.out.println();
        render("--- " + text + " ---", Style.ORANGE);
    }

    public void renderOptions(List<String> options) {
        System.out.println();
        for (int i = 0; i < options.size(); i++) {
            System.out.print("  " + Style.CYAN + (i + 1) + ") " + Style.RESET);
            System.out.println(options.get(i));
        }
    }

    public String prompt(String message) {
        System.out.print("\n" + Style.CYAN + message + " > " + Style.RESET);
        return scanner.nextLine().trim();
    }

    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ----- Flash Messages -----
    // --- The Flash Message Queue ---

    // A small internal class to hold a message and its specific styles
    private static class Flash {
        String message;
        Style[] styles;

        Flash(String message, Style... styles) {
            this.message = message;
            this.styles = styles;
        }
    }

    // The queue of messages waiting to be displayed
    private final List<Flash> flashQueue = new ArrayList<>();

    /**
     * Adds a custom styled message to the flash queue.
     */
    public void addFlash(String message, Style... styles) {
        flashQueue.add(new Flash(message, styles));
    }

    /**
     * Convenience method for a standard Error flash
     */
    public void addFlashError(String message) {
        addFlash("-- " + message, Style.RED, Style.BOLD);
    }

    /**
     * Convenience method for a standard Info/Success flash
     */
    public void addFlashInfo(String message) {
        addFlash("++ " + message, Style.GREEN);
    }

    /**
     * Prints all stored messages in the order they were added, then clears the
     * queue.
     */
    public void renderFlashes() {
        if (flashQueue.isEmpty()) {
            return; // Do nothing if there are no messages
        }

        for (Flash flash : flashQueue) {
            render(flash.message, flash.styles); // Re-use your master render method!
        }

        flashQueue.clear();
    }

    // Region rendering helper
    public void renderAreaOptions(List<Region> regions, int playerXp) {

        System.out.println();

        for (int i = 0; i < regions.size(); i++) {
            Region area = regions.get(i);
            boolean unlocked = area.isUnlocked(playerXp);
            String prefix = "  " + Style.CYAN + (i + 1) + ") " + Style.RESET;

            if (unlocked) {
                System.out.print(prefix);
                render(Style.RESET + "" + Style.BOLD + area.getName() + Style.RESET + 
                ": " + Style.GRAY + area.getDescription());

            } else {
                // Render locked state
                System.out.print(prefix);
                render(Style.BOLD + area.getName() + Style.RESET + Style.RED + " [Locked - Needs " + area.getXp() + " XP]", Style.RED);
            }
        }
    }

}
