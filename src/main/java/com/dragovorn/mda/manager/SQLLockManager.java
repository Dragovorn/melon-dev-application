package com.dragovorn.mda.manager;

import com.dragovorn.mda.Main;
import com.dragovorn.mda.util.Database;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.dragovorn.mda.util.GeneralHelpers.generateKey;

/**
 * Implementation of {@link SimpleLockManager} that uses sql as it's
 * data storage method.
 */
public class SQLLockManager extends SimpleLockManager {

    private static final ExecutorService UPDATE_POOL = Executors.newFixedThreadPool(5, new ThreadFactoryBuilder().setNameFormat("update-%d").build());

    private Database database;

    public SQLLockManager() {
        super();

        Main.getInstance().getLogger().info("Connecting to SQL database...");
        FileConfiguration config = Main.getInstance().getConfig();

        try {
            // Connect to the database the user identified
            this.database = new Database(config.getString("ip"), config.getInt("port"), config.getString("username"), config.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Run some setup sql commands
            Statement statement = this.database.createStatement();

            statement.execute("CREATE DATABASE IF NOT EXISTS " + config.getString("database"));
            statement.execute("USE " + config.getString("database"));
            statement.execute("CREATE TABLE IF NOT EXISTS data (blockKey TEXT, owner CHAR(36))");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            // Populate our back-end Map with the pre-existing data in the database
            Statement statement = this.database.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data");

            while (resultSet.next()) {
                this.data.put(resultSet.getString("blockKey"), UUID.fromString(resultSet.getString("owner")));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Transfer 'legacy' data to our database
        if (FileLockManager.DATA.exists()) {
            Main.getInstance().getLogger().info("Detected old data.json! Transferring data over...");

            try {
                // Read our JSON
                JsonArray file = FileLockManager.GSON.fromJson(new FileReader(FileLockManager.DATA), JsonArray.class);

                // Populate
                file.forEach(element -> {
                    JsonObject object = element.getAsJsonObject();

                    String key = object.get("key").getAsString();
                    UUID owner = UUID.fromString(object.get("owner").getAsString());

                    // Trust pre-existing locks in the database > locks in the 'legacy' file
                    if (this.data.containsKey(key)) {
                        Main.getInstance().getLogger().info("Collision detected! Trusting pre-existing MySQL data...");
                    } else {
                        this.data.put(key, owner);
                        // Post update to our async database updater
                        UPDATE_POOL.execute(new UpdateRunnable("INSERT INTO data VALUES ('" + key + "', '" + owner.toString() + "')"));
                    }
                });

                // Rename our file to prevent reading it again on next startup
                FileLockManager.DATA.renameTo(new File(Main.getInstance().getDataFolder(), "data.json.old"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        try {
            UPDATE_POOL.shutdown();
            UPDATE_POOL.awaitTermination(100, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.database.close();
    }

    @Override
    public boolean unlock(Block block) {
        // Make sure other method gets called and store its result
        boolean unlock = super.unlock(block);

        if (unlock) {
            // Post update to our async database updater
            UPDATE_POOL.execute(new UpdateRunnable("DELETE FROM data WHERE blockKey='" + generateKey(block) + "'"));
        }

        return unlock;
    }

    @Override
    public boolean lock(Player owner, Block block) {
        // Make sure other method gets called and store its result
        boolean lock = super.lock(owner, block);

        if (lock) {
            // Post update to our async database updater
            UPDATE_POOL.execute(new UpdateRunnable("INSERT INTO data VALUES ('" + generateKey(block) + "', '" + owner.getUniqueId().toString() + "')"));
        }

        return lock;
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

    private class UpdateRunnable implements Runnable {

        private final String query;

        private UpdateRunnable(String query) {
            this.query = query;
        }

        @Override
        public void run() {
            try {
                Statement statement = database.createStatement();
                statement.execute(this.query);
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}