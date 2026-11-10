package com.uragiri.keys;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/** 裏切りスキルごとのキー割り当てを追加し、押すと /trigger sk_xxx を送るクライアント用MOD。 */
public class UragiriKeysClient implements ClientModInitializer {
    // データパック側の trigger 目的名 sk_<id> と対応
    private static final String[] IDS = {
        "lockon", "zombie", "randomtp", "invisibility",
        "scatter", "gag", "deathsentence", "baggage", "buff"
    };
    private static final String CATEGORY = "category.uragiri_keys";

    private final KeyBinding[] keys = new KeyBinding[IDS.length];

    @Override
    public void onInitializeClient() {
        for (int i = 0; i < IDS.length; i++) {
            keys[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.uragiri_keys." + IDS[i], InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, CATEGORY));
        }
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (int i = 0; i < IDS.length; i++) {
                while (keys[i].wasPressed()) {
                    if (client.player != null && client.currentScreen == null) {
                        client.player.networkHandler.sendChatCommand("trigger sk_" + IDS[i]);
                    }
                }
            }
        });
    }
}
