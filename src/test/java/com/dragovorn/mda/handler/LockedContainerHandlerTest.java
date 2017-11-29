package com.dragovorn.mda.handler;

import com.dragovorn.mda.Main;
import com.dragovorn.mda.manager.ILockManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LockedContainerHandler.class, Main.class, PlayerInteractEvent.class})
public class LockedContainerHandlerTest {

    @Test
    public void testLocked() {
        Player owner = mock(Player.class);
        when(owner.getName()).thenReturn("Owner");

        ILockManager manager = mock(ILockManager.class);
        when(manager.isLocked(any(Block.class))).thenReturn(true);
        when(manager.getWhoLocked(any(Block.class))).thenReturn(owner);

        Main main = mock(Main.class);
        when(main.getLockManager()).thenReturn(manager);

        mockStatic(Main.class);
        when(Main.getInstance()).thenReturn(main);

        Player clicked = mock(Player.class);

        PlayerInteractEvent interactEvent = mock(PlayerInteractEvent.class);
        when(interactEvent.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(interactEvent.getPlayer()).thenReturn(clicked);
        when(interactEvent.getClickedBlock()).thenReturn(mock(Block.class));

        LockedContainerHandler handler = new LockedContainerHandler();
        handler.checkInteraction(interactEvent);

        verify(clicked, times(1)).sendMessage("This chest is locked by: Owner!");
        verify(interactEvent, times(1)).setCancelled(true);
    }
}