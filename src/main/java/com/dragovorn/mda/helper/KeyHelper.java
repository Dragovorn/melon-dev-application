package com.dragovorn.mda.helper;

import org.bukkit.block.Block;

public class KeyHelper {

    public static String generateKey(Block block) {
        return block.getWorld().getName() + String.valueOf(block.getX()) + String.valueOf(block.getY()) + String.valueOf(block.getZ());
    }
}