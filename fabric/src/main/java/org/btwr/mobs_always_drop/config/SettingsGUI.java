package org.btwr.mobs_always_drop.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.btwr.mobs_always_drop.CommonClass;

public class SettingsGUI {
    public static Screen createConfigScreen(Screen parent) {
        ModSettings settings = CommonClass.settings;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Component.translatable("title.mobs_always_drop.config"));
        builder.setSavingRunnable(CommonClass::saveSettings);

        ConfigEntryBuilder entry = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.mobs_always_drop.category.general"));
        general.addEntry(entry
                .startBooleanToggle(Component.translatable("config.mobs_always_drop.xpDropsEnabled"), settings.xpDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settings.xpDropsEnabled = newValue)
                .build()
        );

        general.addEntry(entry
                .startBooleanToggle(Component.translatable("config.mobs_always_drop.equipmentDropsEnabled"), settings.equipmentDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settings.equipmentDropsEnabled = newValue)
                .build()
        );

        return builder.build();
    }
}