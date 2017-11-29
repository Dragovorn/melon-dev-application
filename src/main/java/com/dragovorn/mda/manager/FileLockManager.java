package com.dragovorn.mda.manager;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class FileLockManager implements ILockManager {
    @Override
    public boolean lock(Block block) {
        return false;
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
    public Player getWhoLocked(Block block) {
        return null;
    }

    @Override
    public String getName() {
        return "File";
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