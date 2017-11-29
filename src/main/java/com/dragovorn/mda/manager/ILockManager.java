package com.dragovorn.mda.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ILockManager {

    boolean lock(Block block);
    boolean unlock(Block block);
    boolean isLocked(Block block);

    Player getWhoLocked(Block block);

    String getName();
    String getDeveloper();
    String getVersion();
}