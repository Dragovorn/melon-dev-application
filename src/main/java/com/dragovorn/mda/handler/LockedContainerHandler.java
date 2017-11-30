package com.dragovorn.mda.handler;

import com.dragovorn.mda.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;

public class LockedContainerHandler implements Listener {

    @EventHandler
    public void checkInteraction(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return; // Make sure a block is being interacted with
        }

        if (!isLockable(event.getClickedBlock())) { // Make sure a block that holds an inventory or is openable is being interacted with
            return; // Make sure block either has an inventory or is an openable
        }

        if (!Main.getInstance().getLockManager().isLocked(event.getClickedBlock())) {
            return;
        }

        if (event.getPlayer().hasPermission("lock.bypass")) {
            return;
        }

        isLocked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(event.getClickedBlock()), event);
    }

    @EventHandler
    public void checkBreak(BlockBreakEvent event) {
        if (!isLockable(event.getBlock())) {
            return;
        }

        if (!Main.getInstance().getLockManager().isLocked(event.getBlock())) {
            return;
        }

        if (event.getPlayer().hasPermission("lock.bypass")) {
            return;
        }

        isLocked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(event.getBlock()), event);
    }

    public static boolean isLockable(Block block) {
        return block.getState() instanceof InventoryHolder || block.getState().getData() instanceof Openable;
    }

    private void isLocked(Player offender, OfflinePlayer owner, Cancellable event) {
        offender.sendMessage("This chest is locked by: " + owner.getName() + "!");
        event.setCancelled(true);
    }
}