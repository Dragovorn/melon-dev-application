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
import java.util.UUID;

/**
 * Implementation of {@link SimpleLockManager} that uses a file as it's
 * data storage method.
 */
public class FileLockManager extends SimpleLockManager {

    // For json file parsing, Gson comes with Spigot
    static final Gson GSON = new GsonBuilder().create();

    static final File DATA = new File(Main.getInstance().getDataFolder(), "data.json");

    public FileLockManager() {
        super();

        // Load pre-existing data, otherwise just save the empty map
        if (DATA.exists()) {
            try {
                // Load data from file (I just like JSON over Yaml)
                JsonArray file = GSON.fromJson(new FileReader(DATA), JsonArray.class);

                // Iterate over our data and put it into our map
                file.forEach(element -> {
                    JsonObject object = element.getAsJsonObject();

                    this.data.put(object.get("key").getAsString(), UUID.fromString(object.get("owner").getAsString()));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    /**
     * Helper to help save data to the data file
     */
    private void save() {
        // Make our writer using Java 8's fancy try resource manager (Shout-out to Andavin for showing me this several months ago in a PR: https://github.com/Dragovorn/util/pull/1)
        try (FileWriter writer = new FileWriter(DATA)) {
            JsonArray file = new JsonArray(); // Make a JsonArray to shove all of our data into

            // Said shoving of data into JsonArray
            this.data.forEach((key, value) -> {
                JsonObject object = new JsonObject();
                object.addProperty("key", key);
                object.addProperty("owner", value.toString());

                file.add(object);
            });

            // Write to our file
            GSON.toJson(file, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        save(); // Save our file on close
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