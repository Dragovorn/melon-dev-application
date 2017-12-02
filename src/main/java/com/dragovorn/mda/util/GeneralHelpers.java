package com.dragovorn.mda.util;

import com.dragovorn.mda.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Door;
import org.bukkit.material.Openable;

import java.util.Arrays;
import java.util.List;

public class GeneralHelpers {

    // Just to make checking the adjacent blocks easier
    private static final List<BlockFace> CHEST_FACES = Arrays.asList(BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST);
    private static final List<BlockFace> DOOR_FACES = Arrays.asList(BlockFace.DOWN, BlockFace.UP);

    public static Block getAdjacent(Block block, boolean locked) {
        List<BlockFace> list = null;
        Class desired = null;

        // Figure out what to check for
        if (block.getState() instanceof Chest) {
            list = CHEST_FACES;
            desired = Chest.class;
        } else if (block.getState().getData() instanceof Door) {
            list = DOOR_FACES;
            desired = Door.class;
        }

        // If it couldn't find a list just return null
        if (list == null) {
            return null;
        }

        // Actually iterate and look
        for (BlockFace face : list) {
            Block relativeBlock = block.getRelative(face);

            if (relativeBlock.getState().getClass().isAssignableFrom(desired)) {
                if (Main.getInstance().getLockManager().isLocked(relativeBlock) == locked) {
                    return relativeBlock;
                }
            }
        }

        return null;
    }

    public static boolean isLockable(Block block) { // Since everything we want to lock is either an InventoryHolder or an Openable just check for those
        return block.getState() instanceof InventoryHolder || block.getState().getData() instanceof Openable;
    }

    // Just a simple colourize method
    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Generate our block unique key
    public static String generateKey(Block block) {
        return block.getWorld().getName() + "," + String.valueOf(block.getX()) + "," + String.valueOf(block.getY()) + "," + String.valueOf(block.getZ());
    }
}