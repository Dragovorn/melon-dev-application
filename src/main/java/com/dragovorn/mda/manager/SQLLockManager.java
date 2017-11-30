package com.dragovorn.mda.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class SQLLockManager implements ILockManager {

    @Override
    public void close() {

    }

    @Override
    public boolean unlock(Block block) {
        return false;
    }

    @Override
    public boolean isLocked(Block block) {
        return false;
    }

    @Override
    public boolean lock(Player owner, Block block) {
        return false;
    }

    @Override
    public OfflinePlayer getWhoLocked(Block block) {
        return null;
    }

    @Override
    public String getName() {
        return "SQL";
    }

    @Override
    public String getDeveloper() {
        return "Dragovorn";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}