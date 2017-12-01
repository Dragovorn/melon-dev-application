package com.dragovorn.mda.manager;

import com.dragovorn.mda.Main;
import com.dragovorn.mda.util.Database;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class SQLLockManager implements ILockManager {

    private Database database;

    public SQLLockManager() {
        FileConfiguration config = Main.getInstance().getConfig();

        try {
            this.database = new Database(config.getString("ip"), config.getInt("port"), config.getString("database"), config.getString("user"), config.getString("pass"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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