package com.example;

import com.example.screen.ModSettingsScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

public class TemplateModClient implements ClientModInitializer {
    
    public static final Identifier RAIN_ICON_TEXTURE = new Identifier(TemplateMod.MOD_ID, "textures/gui/rain_icon.png");
    
    public static final KeyBinding OPEN_SETTINGS_KEY = KeyBindingHelper.registerKeyBinding(
        new KeyBinding(
            "key.ever-j.open_settings",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "category.ever-j"
        )
    );

    @Override
    public void onInitializeClient() {
        
        ModSettingsScreen.load();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_SETTINGS_KEY.wasPressed()) {
                client.setScreen(ModSettingsScreen.create(client.currentScreen));
            }
        });

        HudRenderCallback.EVENT.register((DrawContext context, float tickDelta) -> {
            if (!ModSettingsScreen.drawRainIcon) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null && client.world.isRaining() && client.world.getRegistryKey() == World.OVERWORLD) {
                int size = 32;
                int x = client.getWindow().getScaledWidth() - size - 5;
                int y = 10;
                context.drawTexture(RAIN_ICON_TEXTURE, x, y, 0, 0, size, size, size, size);
            }
        });
    }
}