package com.dragovorn.mda.handler;

import com.dragovorn.mda.Main;
import com.dragovorn.mda.manager.ILockManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;
import java.util.logging.Logger;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LockedContainerHandler.class, Main.class, PlayerInteractEvent.class, BlockBreakEvent.class})
public class TestLockedContainerHandler {

    private BlockState door;

    @Before
    public void before() {
        this.door = mock(BlockState.class);
        when(this.door.getData()).thenReturn(mock(Door.class));
    }

    @Test
    public void testNotChest() {
        test(true, true, mock(BlockState.class), false);
    }

    @Test
    public void testLockedChestNoPerm() {
        test(true, false, mock(Chest.class), false);
    }

    @Test
    public void testLockedChestWithPerm() {
        test(true, true, mock(Chest.class), true);
    }

    @Test
    public void testUnlockedChestNoPerm() {
        test(false, true, mock(Chest.class), false);
    }

    @Test
    public void testUnlockedChestWithPerm() {
        test(false, true, mock(Chest.class), true);
    }

    @Test
    public void testLockedDoorNoPerm() {
        test(true, false, this.door, false);
    }

    @Test
    public void testLockedDoorWithPerm() {
        test(true, true, this.door, true);
    }

    @Test
    public void testUnlockedDoorNoPerm() {
        test(false, true, this.door, false);
    }

    @Test
    public void testUnlockedDoorWithPerm() {
        test(false, true, this.door, true);
    }

    private void test(boolean locked, boolean allowed, BlockState state, boolean hasPermission) {
        UUID uuid = UUID.randomUUID();

        OfflinePlayer owner = mock(OfflinePlayer.class);
        when(owner.getName()).thenReturn("Owner");
        when(owner.getUniqueId()).thenReturn(uuid);

        ILockManager manager = mock(ILockManager.class);
        when(manager.isLocked(any(Block.class))).thenReturn(locked);
        when(manager.getWhoLocked(any(Block.class))).thenReturn(owner);

        Main main = mock(Main.class);
        when(main.getLockManager()).thenReturn(manager);
        when(main.getLogger()).thenReturn(mock(Logger.class));

        mockStatic(Main.class);
        when(Main.getInstance()).thenReturn(main);

        if (!allowed) {
            uuid = UUID.randomUUID();
        }

        Player offender = mock(Player.class);
        when(offender.hasPermission(anyString())).thenReturn(hasPermission);
        when(offender.getUniqueId()).thenReturn(uuid);

        Block relative = mock(Block.class);
        when(relative.getState()).thenReturn(state);

        Block block = mock(Block.class);
        when(block.getState()).thenReturn(state);
        when(block.getRelative(any(BlockFace.class))).thenReturn(relative);

        PlayerInteractEvent interactEvent = mock(PlayerInteractEvent.class);
        when(interactEvent.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(interactEvent.getPlayer()).thenReturn(offender);
        when(interactEvent.getClickedBlock()).thenReturn(block);

        BlockBreakEvent breakEvent = mock(BlockBreakEvent.class);
        when(breakEvent.getPlayer()).thenReturn(offender);
        when(breakEvent.getBlock()).thenReturn(block);

        LockedContainerHandler handler = new LockedContainerHandler();
        handler.checkInteraction(interactEvent);
        handler.checkBreak(breakEvent);

        if (!allowed) {
            verify(offender, times(2)).sendMessage("§cThis block is locked by: §eOwner§c!");
            verify(interactEvent, times(1)).setCancelled(true);
            verify(breakEvent, times(1)).setCancelled(true);
        } else {
            verify(offender, times(0)).sendMessage("§cThis block is locked by: §eOwner§c!");
            verify(interactEvent, times(0)).setCancelled(true);
            verify(breakEvent, times(0)).setCancelled(true);
        }
    }
}