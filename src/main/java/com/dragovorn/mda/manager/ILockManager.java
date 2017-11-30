package com.dragovorn.mda.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ILockManager {

    void close();

    boolean unlock(Block block);
    boolean isLocked(Block block);
    boolean lock(Player owner, Block block);

    OfflinePlayer getWhoLocked(Block block);

    String getName();
    String getDeveloper();
    String getVersion();
}