package ivangeevo.mobsalwaysdrop.config;

import ivangeevo.mobsalwaysdrop.MobsAlwaysDropMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class SettingsGUI
{
    static ModSettings settingsCommon = MobsAlwaysDropMod.getInstance().settings;
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Text.translatable("title.mobsalwaysdrop.config"));
        builder.setSavingRunnable(() -> {
            MobsAlwaysDropMod.getInstance().saveSettings();


        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.mobsalwaysdrop.category.general"));

        /** General Category**/


        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("config.mobsalwaysdrop.xpDropsEnabled"), settingsCommon.xpDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.xpDropsEnabled = newValue)
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(
                        Text.translatable("config.mobsalwaysdrop.equipmentDropsEnabled"), settingsCommon.equipmentDropsEnabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.equipmentDropsEnabled = newValue)
                .build());


        return builder.build();
    }




    public static Text[] wrapLines(Text text){
        List<StringVisitable> lines = MinecraftClient.getInstance().textRenderer.getTextHandler()
                .wrapLines(text,Math.max(MinecraftClient.getInstance().getWindow().getScaledWidth()/2 - 43,170),
                        Style.EMPTY);
        lines.get(0).getString();
        Text[] textLines = new Text[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            textLines[i]=Text.literal(lines.get(i).getString());
        }
        return textLines;
    }



}