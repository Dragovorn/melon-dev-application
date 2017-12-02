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

    private static final List<BlockFace> CHEST_FACES = Arrays.asList(BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST);
    private static final List<BlockFace> DOOR_FACES = Arrays.asList(BlockFace.DOWN, BlockFace.UP);

    public static Block getAdjacent(Block block, boolean locked) {
        if (block.getState() instanceof Chest) {
            for (BlockFace face : CHEST_FACES) {
                Block relativeBlock = block.getRelative(face);

                if (relativeBlock.getState() instanceof Chest) {
                    if (Main.getInstance().getLockManager().isLocked(relativeBlock) == locked) {
                        return relativeBlock;
                    }
                }
            }
        } else if (block.getState().getData() instanceof Door) {
            for (BlockFace face : DOOR_FACES) {
                Block relativeBlock = block.getRelative(face);

                if (relativeBlock.getState().getData() instanceof Door) {
                    if (Main.getInstance().getLockManager().isLocked(relativeBlock) == locked) {
                        return relativeBlock;
                    }
                }
            }
        }

        return null;
    }

    public static boolean isLockable(Block block) {
        return block.getState() instanceof InventoryHolder || block.getState().getData() instanceof Openable;
    }

    public static String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String generateKey(Block block) {
        return block.getWorld().getName() + "," + String.valueOf(block.getX()) + "," + String.valueOf(block.getY()) + "," + String.valueOf(block.getZ());
    }
}