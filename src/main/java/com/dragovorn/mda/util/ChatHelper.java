package com.dragovorn.mda.util;

import org.bukkit.ChatColor;

public class ChatHelper {

    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}