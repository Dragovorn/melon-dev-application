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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import static com.dragovorn.mda.util.GeneralHelpers.colourize;
import static com.dragovorn.mda.util.GeneralHelpers.getAdjacent;
import static com.dragovorn.mda.util.GeneralHelpers.isLockable;

public class LockedContainerHandler implements Listener {

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
            return; // Block isn't locked
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
            return;
        }

        locked(event.getPlayer(), Main.getInstance().getLockManager().getWhoLocked(block), event);
    }

    @EventHandler
    public void registerNewLocks(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (!isLockable(block)) {
            return;
        }

        Block locked = getAdjacent(block, true);

        if (locked != null) {
            Main.getInstance().getLockManager().lock(event.getPlayer(), block);
            event.getPlayer().sendMessage(colourize("&aSince you made a double chest with a locked chest the chest you placed has been automatically locked!"));
        }
    }

    private void locked(Player offender, OfflinePlayer owner, Cancellable event) {
        if (event instanceof BlockBreakEvent) {
            if (offender.hasPermission("lock.bypass") || offender.getUniqueId().equals(owner.getUniqueId())) {
                Main.getInstance().getLockManager().unlock(((BlockBreakEvent) event).getBlock());
                offender.sendMessage(colourize("&aYou broke a locked block! It's been unlocked automatically!"));

                return;
            }
        } else {
            if (offender.hasPermission("lock.bypass") || offender.getUniqueId().equals(owner.getUniqueId())) {
                offender.sendMessage(colourize("&aAccessing locked block..."));
                return;
            }
        }

        offender.sendMessage(colourize("&cThis block is locked by: &e" + owner.getName() + "&c!"));
        event.setCancelled(true);
    }
}