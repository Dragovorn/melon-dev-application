package com.dragovorn.mda.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.dragovorn.mda.util.GeneralHelpers.generateKey;
import static org.bukkit.Bukkit.getOfflinePlayer;

public abstract class SimpleLockManager implements ILockManager {

    protected Map<String, UUID> data;

    public SimpleLockManager() {
        this.data = new HashMap<>();
    }

    @Override
    public boolean unlock(Block block) {
        if (!isLocked(block)) {
            return false;
        }

        this.data.remove(generateKey(block));

        return true;
    }

    @Override
    public boolean isLocked(Block block) {
        return this.data.containsKey(generateKey(block));
    }

    @Override
    public boolean lock(Player owner, Block block) {
        if (isLocked(block)) {
            return false;
        }

        this.data.put(generateKey(block), owner.getUniqueId());

        return true;
    }

    @Override
    public OfflinePlayer getWhoLocked(Block block) {
        if (!isLocked(block)) {
            return null;
        }

        return getOfflinePlayer(this.data.get(generateKey(block)));
    }
}