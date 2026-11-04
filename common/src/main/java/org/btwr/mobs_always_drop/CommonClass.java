package org.btwr.mobs_always_drop;

import com.google.gson.Gson;
import org.btwr.mobs_always_drop.config.ModSettings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Shared startup and settings code, called by each loader's entrypoint.
 */
public class CommonClass {
    public static ModSettings settings;

    public static void init() {
        loadSettings();
    }

    public static void loadSettings() {
        File file = new File("./config/btwr/mobsAlwaysDropCommon.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, ModSettings.class);
                fileReader.close();
            } catch (IOException e) {
                Constants.LOG.warn("Could not load Mobs Always Drops settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new ModSettings();
        }
    }

    public static void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/btwr/mobsAlwaysDropCommon.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            Constants.LOG.warn("Could not save Mobs Always Drops settings: " + e.getLocalizedMessage());
        }
    }
}