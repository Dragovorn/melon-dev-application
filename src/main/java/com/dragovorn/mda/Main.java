package com.dragovorn.mda;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance; // Plugins are singletons

    @Override
    public void onLoad() {
        instance = this;

        if (getDataFolder().mkdirs()) {
            getLogger().info("Created data folder!"); // Just some logging
        }

        saveDefaultConfig(); // Of course we need to create our config file
    }

    @Override
    public void onEnable() {

    }

    public static Main getInstance() {
        return instance;
    }
}