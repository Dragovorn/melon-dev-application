package com.dragovorn.mda.manager;

import com.dragovorn.mda.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class FileLockManager extends SimpleLockManager {

    static final Gson GSON = new GsonBuilder().create();

    static final File DATA = new File(Main.getInstance().getDataFolder(), "data.json");

    public FileLockManager() {
        super();
        if (DATA.exists()) {
            try {
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