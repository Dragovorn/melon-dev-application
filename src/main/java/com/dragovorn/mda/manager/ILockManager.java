package com.dragovorn.mda.manager;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface ILockManager {

    /**
     * Called in onDisable when the plugin is being disabled
     * used for serialization of LockManager data
     */
    void close();

    /**
     * Attempts to unlock the given block
     *
     * @param block the given block
     * @return if the given block was actually unlocked
     */
    boolean unlock(Block block);

    /**
     * Checks if the given block is locked
     *
     * @param block the given block
     * @return if the given block is locked
     */
    boolean isLocked(Block block);

    /**
     * Attempts to lock the given block under the name of
     * the given player
     *
     * @param owner the given player
     * @param block the given block
     * @return if the given block was locked
     */
    boolean lock(Player owner, Block block);

    /**
     * Gets the owner of the given block, returns null
     * if the given block isn't locked
     *
     * @param block the given block
     * @return the owner of the given block, returns null if the given block isn't locked
     */
    OfflinePlayer getWhoLocked(Block block);

    /**
     * Used for documenting the LockManager
     *
     * @return the name of the LockManager
     */
    String getName();

    /**
     * Used for documenting the LockManager
     *
     * @return the name of the developer who made the LockManager
     */
    String getDeveloper();

    /**
     * Used for documenting the LockManager
     *
     * @return the version the LockManager
     */
    String getVersion();
}