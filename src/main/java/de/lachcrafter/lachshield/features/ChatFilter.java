package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.List;

public class ChatFilter extends Feature {
    private final List<String> blacklist = LachShield.configManager.getStringList("ChatFilter.blacklist");
    private final LachShield lachShield;

    public ChatFilter(LachShield lachShield) {
        super("ChatFilter", true);
        this.lachShield = lachShield;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        if (hasFeaturePermission(event.getPlayer())) return;

        for (String s : blacklist) {
            if (event.message().toString().toLowerCase().contains(s.toLowerCase())) {
                event.setCancelled(true);
                if (LachShield.configManager.getConfig().getBoolean("ChatFilter.warning.enabled")) {
                    event.getPlayer().sendMessage(LachShield.configManager.getMessage("ChatFilter.warning.message"));
                }
                break;
            }
        }
    }

    @Override
    public void onEnable() {
        lachShield.getServer().getPluginManager().registerEvents(this, lachShield);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onReload() {}
}
