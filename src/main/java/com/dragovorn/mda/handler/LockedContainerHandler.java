package com.dragovorn.mda.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LockedContainerHandler {

    @EventHandler
    public void checkInteraction(PlayerInteractEvent event) {
        // TODO check player interaction with the container
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