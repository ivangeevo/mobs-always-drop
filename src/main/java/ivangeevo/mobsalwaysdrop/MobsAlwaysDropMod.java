package ivangeevo.mobsalwaysdrop;

import com.google.gson.Gson;
import ivangeevo.mobsalwaysdrop.config.ModSettings;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class MobsAlwaysDropMod implements ModInitializer
{

    public static final String MOD_ID = "mobs_always_drop";
    public static final Logger LOGGER = LoggerFactory.getLogger("mobs_always_drop");

    private static final File configFile = new File("config/mobsalwaysdrop.properties");
    public ModSettings settings;
    private static MobsAlwaysDropMod instance;

    public static MobsAlwaysDropMod getInstance() {
        return instance;
    }

    @Override
    public void onInitialize()
    {
        // Load settings when the mod initializes
        loadSettings();
        instance = this;
    }

    public void loadSettings() {
        File file = new File("./config/btwr/mobsAlwaysDropCommon.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, ModSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load Mobs Always Drops settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new ModSettings();
        }
    }

    public void saveSettings() {
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
            LOGGER.warn("Could not save Tough Environment settings: " + e.getLocalizedMessage());
        }
    }

}


