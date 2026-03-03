package com.example.screen;

import com.example.TemplateMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.io.File;

public class ModSettingsScreen {
    
    public static final File CONFIG_FILE = new File(
        System.getProperty("user.dir"), 
        "config/ever-j.json"
    );
    
    public static boolean drawRainIcon = true;
    
    public static void load() {
        if (CONFIG_FILE.exists()) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(CONFIG_FILE.toPath()));
                com.google.gson.JsonObject json = com.google.gson.JsonParser.parseString(content).getAsJsonObject();
                if (json.has("drawRainIcon")) {
                    drawRainIcon = json.get("drawRainIcon").getAsBoolean();
                }
                TemplateMod.LOGGER.info("[Ever J] Config loaded: drawRainIcon = " + drawRainIcon);
            } catch (Exception e) {
                TemplateMod.LOGGER.error("[Ever J] Failed to load config!", e);
            }
        } else {
            save();
        }
    }
    
    public static void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            com.google.gson.JsonObject json = new com.google.gson.JsonObject();
            json.addProperty("drawRainIcon", drawRainIcon);
            java.nio.file.Files.write(CONFIG_FILE.toPath(), json.toString().getBytes());
            TemplateMod.LOGGER.info("[Ever J] Config saved!");
        } catch (Exception e) {
            TemplateMod.LOGGER.error("[Ever J] Failed to save config!", e);
        }
    }
    
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.translatable("screen.ever-j.settings.title"));
        
        builder.setSavingRunnable(ModSettingsScreen::save);
        
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.ever-j.general"));
        
        category.addEntry(builder.entryBuilder()
            .startBooleanToggle(Text.translatable("screen.ever-j.settings.toggle"), drawRainIcon)
            .setDefaultValue(true)
            .setSaveConsumer(newValue -> drawRainIcon = newValue)  // ← Правильный метод!
            .build());
        
        return builder.build();
    }
}