package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.lib.Feature;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.List;

public class ChatFilter implements Feature {
    private final List<String> blacklist = LachShield.configManager.getStringList("ChatFilter.blacklist");
    private final LachShield lachShield;

    public ChatFilter(LachShield lachShield) {
        this.lachShield = lachShield;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        for (String s : blacklist) {
            if (event.message().toString().contains(s)) {
                event.setCancelled(true);
                if (LachShield.configManager.getConfig().getBoolean("ChatFilter.warning.enabled")) {
                    event.getPlayer().sendMessage(LachShield.configManager.getMessage("ChatFilter.warning.message"));
                }
                break;
            }
        }
    }

    @Override
    public String getFeatureName() {
        return "ChatFilter";
    }

    @Override
    public void enable() {
        lachShield.getServer().getPluginManager().registerEvents(this, lachShield);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void reload() {}
}
