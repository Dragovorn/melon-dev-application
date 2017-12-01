package com.dragovorn.mda.helper;

import org.bukkit.ChatColor;

public class ChatHelper {

    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}