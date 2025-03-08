package ivangeevo.mobs_always_drop.config;

import ivangeevo.mobs_always_drop.MobsAlwaysDropMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SettingsGUI {

    static ModSettings settingsCommon = MobsAlwaysDropMod.getInstance().settings;
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Text.translatable("title.mobs_always_drop.config"));
        builder.setSavingRunnable(() -> {
            MobsAlwaysDropMod.getInstance().saveSettings();

        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.mobs_always_drop.category.general"));

        /** General Category**/
        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("config.mobs_always_drop.xpDropsEnabled"), settingsCommon.xpDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.xpDropsEnabled = newValue)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("config.mobs_always_drop.equipmentDropsEnabled"), settingsCommon.equipmentDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.equipmentDropsEnabled = newValue)
                .build());


        return builder.build();
    }

}