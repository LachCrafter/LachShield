package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.lib.Feature;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;

import java.util.List;

public class ChatCensor implements Feature {
    private final List<String> blacklist = LachShield.configManager.getStringList("chatCensor.blacklist");
    private final LachShield lachShield;

    public ChatCensor(LachShield lachShield) {
        this.lachShield = lachShield;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        for (String s : blacklist) {
            if (event.message().toString().contains(s)) {
                event.setCancelled(true);
                if (LachShield.configManager.getConfig().getBoolean("chatCensor.warning.enabled")) {
                    event.getPlayer().sendMessage(LachShield.configManager.getMessage("chatCensor.warning.message"));
                }
                break;
            }
        }
    }

    @Override
    public String getFeatureName() {
        return "ChatCensor";
    }

    @Override
    public void enable() {
        lachShield.getServer().getPluginManager().registerEvents(this, lachShield);
    }

    @Override
    public void disable() {}

    @Override
    public void reload() {}
}
