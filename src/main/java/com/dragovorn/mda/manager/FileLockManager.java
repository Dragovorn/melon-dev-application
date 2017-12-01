package com.dragovorn.mda.manager;

import com.dragovorn.mda.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.dragovorn.mda.util.KeyHelper.generateKey;
import static org.bukkit.Bukkit.getOfflinePlayer;

public class FileLockManager implements ILockManager {

    private Map<String, UUID> data;

    private static final Gson GSON = new GsonBuilder().create();

    private static final File DATA = new File(Main.getInstance().getDataFolder(), "data.json");

    public FileLockManager() {
        if (DATA.exists()) {
            try {
                this.data = new HashMap<>();
                JsonArray file = GSON.fromJson(new FileReader(DATA), JsonArray.class);

                file.forEach(element -> {
                    JsonObject object = element.getAsJsonObject();

                    this.data.put(object.get("key").getAsString(), UUID.fromString(object.get("owner").getAsString()));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.data = new HashMap<>();
            save();
        }
    }

    private void save() {
        try (FileWriter writer = new FileWriter(DATA)) {
            JsonArray file = new JsonArray();

            this.data.forEach((key, value) -> {
                JsonObject object = new JsonObject();
                object.addProperty("key", key);
                object.addProperty("owner", value.toString());

                file.add(object);
            });

            GSON.toJson(file, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        save();
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