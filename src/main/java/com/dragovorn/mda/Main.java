package com.dragovorn.mda;

import com.dragovorn.mda.command.HelpExecutor;
import com.dragovorn.mda.command.LockExecutor;
import com.dragovorn.mda.exception.MalformedLockType;
import com.dragovorn.mda.exception.UnsupportedLockType;
import com.dragovorn.mda.handler.LockedContainerHandler;
import com.dragovorn.mda.manager.FileLockManager;
import com.dragovorn.mda.manager.ILockManager;
import com.dragovorn.mda.manager.SQLLockManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin {

    private ILockManager lockManager;

    private Map<String, Class<? extends ILockManager>> lockManagerTypes; // We want this to be as expandable as possible

    private static Main instance; // Plugins are singletons

    @Override
    public void onLoad() {
        instance = this;
        this.lockManagerTypes = new HashMap<>();

        if (getDataFolder().mkdirs()) {
            getLogger().info("Created data folder!"); // Just some logging
        }

        this.lockManagerTypes.put("file", FileLockManager.class);
        this.lockManagerTypes.put("sql", SQLLockManager.class);

        saveDefaultConfig(); // Of course we need to create our config file
    }

    @Override
    public void onEnable() {
        try {
            this.lockManager = setLockManager();
        } catch (UnsupportedLockType unsupportedLockType) {
            unsupportedLockType.printStackTrace(); // Do this so our errors propagate
        }

        getLogger().info("Using lock type: " + this.lockManager.getName() + " v" + this.lockManager.getVersion() + " developed by: " + this.lockManager.getDeveloper());

        registerCommand("lock", LockExecutor.class);
        registerCommand("lockhelp", HelpExecutor.class);

        registerListener(LockedContainerHandler.class);
    }

    @Override
    public void onDisable() {
        this.lockManager.close();
    }

    private void registerListener(Class<? extends Listener> listener) { // Allows for simpler listener registration
        try {
            Bukkit.getPluginManager().registerEvents(listener.newInstance(), this);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerCommand(String command, Class<? extends CommandExecutor> executor) { // Allows for simpler command registration
        try {
            getCommand(command).setExecutor(executor.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ILockManager setLockManager() throws UnsupportedLockType {
        if (this.lockManagerTypes.containsKey(getConfig().getString("storage"))) { // Make sure the provided lock type is supported
            try {
                return this.lockManagerTypes.get(getConfig().getString("storage")).newInstance(); // Create an instance of the LockManager that goes with it
            } catch (Throwable e) {
                throw new MalformedLockType(getConfig().getString("storage"), e); // Provided lock type is broken
            }
        }

        throw new UnsupportedLockType(getConfig().getString("storage")); // Unsupported lock type
    }

    public static Main getInstance() {
        return instance;
    }

    public ILockManager getLockManager() {
        return this.lockManager;
    }
}