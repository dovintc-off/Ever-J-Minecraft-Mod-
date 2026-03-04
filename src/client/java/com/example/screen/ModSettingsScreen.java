package com.example.screen;

import com.example.TemplateMod;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.client.util.InputUtil;
import java.io.File;
import java.nio.file.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lwjgl.glfw.GLFW;

public class ModSettingsScreen {
    
    public static final File CONFIG_FILE = new File(
        System.getProperty("user.dir"), 
        "config/ever-j.json"
    );
    
    public static boolean drawRainIcon = true;
    public static int rainIconSize = 32;
    public static InputUtil.Key standActivateKey = InputUtil.fromKeyCode(GLFW.GLFW_KEY_J, -1);
    
    // Загрузка
    public static void load() {
        if (CONFIG_FILE.exists()) {
            try {
                String content = new String(Files.readAllBytes(CONFIG_FILE.toPath()));
                JsonObject json = JsonParser.parseString(content).getAsJsonObject();
                
                if (json.has("drawRainIcon")) {
                    drawRainIcon = json.get("drawRainIcon").getAsBoolean();
                }
                if (json.has("rainIconSize")) {
                    rainIconSize = json.get("rainIconSize").getAsInt();
                }
                if (json.has("standActivateKeyCode")) {
                    int keyCode = json.get("standActivateKeyCode").getAsInt();
                    standActivateKey = InputUtil.fromKeyCode(keyCode, -1);
                }
                
                TemplateMod.LOGGER.info("[Ever J] Config loaded");
            } catch (Exception e) {
                TemplateMod.LOGGER.error("[Ever J] Failed to load config!", e);
            }
        } else {
            save();
        }
    }
    
    // Сохранение
    public static void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            JsonObject json = new JsonObject();
            json.addProperty("drawRainIcon", drawRainIcon);
            json.addProperty("rainIconSize", rainIconSize);
            json.addProperty("standActivateKeyCode", standActivateKey.getCode());
            Files.write(CONFIG_FILE.toPath(), json.toString().getBytes());
            TemplateMod.LOGGER.info("[Ever J] Config saved!");
        } catch (Exception e) {
            TemplateMod.LOGGER.error("[Ever J] Failed to save config!", e);
        }
    }
    
    // Создание экрана
    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.translatable("screen.ever-j.settings.title"));
        
        builder.setSavingRunnable(ModSettingsScreen::save);
        
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.ever-j.general"));
        
        category.addEntry(builder.entryBuilder()
            .startBooleanToggle(Text.translatable("screen.ever-j.settings.toggle"), drawRainIcon)
            .setDefaultValue(true)
            .setSaveConsumer(newValue -> drawRainIcon = newValue)
            .build());
        
        category.addEntry(builder.entryBuilder()
            .startIntSlider(Text.translatable("screen.ever-j.settings.sizeIcon"), rainIconSize, 8, 64)
            .setDefaultValue(16)
            .setSaveConsumer(newValue -> rainIconSize = newValue)
            .build());
        
        category.addEntry(builder.entryBuilder()
            .startKeyCodeField(Text.translatable("screen.ever-j.settings.stand_key"), standActivateKey)
            .setDefaultValue(InputUtil.fromKeyCode(GLFW.GLFW_KEY_J, -1))
            .build());
        
        return builder.build();
    }
}