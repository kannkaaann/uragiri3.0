package com.uragiri.keys;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import de.maxhenkel.voicechat.api.events.MicrophonePacketEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Voice Chat 用アドオン。ホスト(サーバー側)で動作し、
 * タグ "svc_mute" が付いたプレイヤーの音声を破棄(ミュート)する。
 */
public class MutePlugin implements VoicechatPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger("uragiri_keys");
    private boolean loggedMute = false;

    @Override
    public void initialize(VoicechatApi api) {
        LOGGER.info("[uragiri_keys] Simple Voice Chat plugin loaded");
    }

    @Override
    public String getPluginId() {
        return "uragiri_keys";
    }

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(MicrophonePacketEvent.class, this::onMicrophone);
    }

    private void onMicrophone(MicrophonePacketEvent event) {
        VoicechatConnection connection = event.getSenderConnection();
        if (connection == null) {
            return;
        }
        Object player = connection.getPlayer().getPlayer();
        if (player instanceof ServerPlayerEntity serverPlayer
                && serverPlayer.getCommandTags().contains("svc_mute")) {
            if (!loggedMute) {
                LOGGER.info("[uragiri_keys] muting voice of {}", serverPlayer.getName().getString());
                loggedMute = true;
            }
            event.cancel();
        }
    }
}
