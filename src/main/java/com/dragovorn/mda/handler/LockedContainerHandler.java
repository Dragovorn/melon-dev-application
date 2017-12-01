package com.dragovorn.mda.handler;

import com.dragovorn.mda.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Door;
import org.bukkit.material.Openable;

import java.util.Arrays;
import java.util.List;

public class LockedContainerHandler implements Listener {

    private List<BlockFace> chestFaces = Arrays.asList(BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST);
    private List<BlockFace> doorFaces = Arrays.asList(BlockFace.DOWN, BlockFace.UP);

    @EventHandler
    public void checkInteraction(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return; // Make sure a block is being interacted with
        }

        Block block = event.getClickedBlock();

        if (!isLockable(block)) { // Make sure a block that holds an inventory or is openable is being interacted with
            return; // Make sure block either has an inventory or is an openable
        }

        if (!Main.getInstance().getLockManager().isLocked(block)) {
            if (block.getState() instanceof Chest) {
                for (BlockFace face : this.chestFaces) {
                    Block relativeBlock = block.getRelative(face);

                    if (relativeBlock.getState() instanceof Chest) {
                        if (Main.getInstance().getLockManager().isLocked(relativeBlock)) {
                            locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(relativeBlock), event);
                            return;
                        }
                    }
                }
            }

            if (block.getState().getData() instanceof Door) {
                for (BlockFace face : this.doorFaces) {
                    Block relativeBlock = block.getRelative(face);

                    if (relativeBlock.getState().getData() instanceof Door) {
                        if (Main.getInstance().getLockManager().isLocked(relativeBlock)) {
                            locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(relativeBlock), event);
                            return;
                        }
                    }
                }
            }

            return;
        }

        locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(block), event);
    }

    @EventHandler
    public void checkBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (!isLockable(block)) {
            return;
        }

        if (!Main.getInstance().getLockManager().isLocked(block)) {
            if (block.getState() instanceof Chest) {
                for (BlockFace face : this.chestFaces) {
                    Block relativeBlock = block.getRelative(face);

                    if (relativeBlock.getState() instanceof Chest) {
                        if (Main.getInstance().getLockManager().isLocked(relativeBlock)) {
                            locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(relativeBlock), event);
                            return;
                        }
                    }
                }
            }

            if (block.getState().getData() instanceof Door) {
                for (BlockFace face : this.doorFaces) {
                    Block relativeBlock = block.getRelative(face);

                    if (relativeBlock.getState().getData() instanceof Door) {
                        if (Main.getInstance().getLockManager().isLocked(relativeBlock)) {
                            locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(relativeBlock), event);
                            return;
                        }
                    }
                }
            }

            return;
        }

        locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(block), event);
    }

    public static boolean isLockable(Block block) {
        return block.getState() instanceof InventoryHolder || block.getState().getData() instanceof Openable;
    }

    private void locked(Player offender, OfflinePlayer owner, Cancellable event) {
        if (event instanceof BlockBreakEvent) {
            if (offender.hasPermission("lock.bypass") || offender.getUniqueId().equals(owner.getUniqueId())) {
                Main.getInstance().getLockManager().unlock(((BlockBreakEvent) event).getBlock());
                offender.sendMessage("You broke a locked block! It's been unlocked automatically!");

                return;
            }
        } else {
            if (offender.hasPermission("lock.bypass") || offender.getUniqueId().equals(owner.getUniqueId())) {
                offender.sendMessage("Accessing locked block...");
                return;
            }
        }

        offender.sendMessage("This block is locked by: " + owner.getName() + "!");
        event.setCancelled(true);
    }
}