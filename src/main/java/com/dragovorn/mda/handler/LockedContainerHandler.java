package com.dragovorn.mda.handler;

import com.dragovorn.mda.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.Openable;

public class LockedContainerHandler implements Listener {

    @EventHandler
    public void checkInteraction(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) { // Make sure a block is being interacted with
            return;
        }

        if (!(event.getClickedBlock().getState() instanceof InventoryHolder || event.getClickedBlock().getState().getData() instanceof Openable)) { // Make sure a block that holds an inventory or is openable is being interacted with
            return;
        }

        if (Main.getInstance().getLockManager().isLocked(event.getClickedBlock())) {
            event.getPlayer().sendMessage("This chest is locked by: " + Main.getInstance().getLockManager().getWhoLocked(event.getClickedBlock()).getName() + "!");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void checkBreak(BlockBreakEvent event) {
        // TODO check player block break with the container
    }

    @EventHandler
    public void checkPlace(BlockPlaceEvent event) {
        // TODO check player block place with the container
    }
}