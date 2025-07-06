package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.lib.NewFeature;
import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.LachShield;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IPAccountManager extends NewFeature {
    private final LachShield plugin;
    private final ConfigManager configManager;
    private final Map<String, Integer> ipAccountCount = new HashMap<>();
    private int maxAccountsPerIP;

    public IPAccountManager(LachShield plugin, ConfigManager configManager) {
        super("IPLimiter", true);
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount >= maxAccountsPerIP) {
            Component kickComponent = configManager.getMessage("IPLimiter.kickMessage");
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickComponent);
            return;
        }

        ipAccountCount.put(ip, accountCount + 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        handlePlayerQuit(event.getPlayer());
    }

    public void handlePlayerQuit(Player player) {
        String ip = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount > 0) {
            ipAccountCount.put(ip, accountCount - 1);
        }
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onReload() {
        maxAccountsPerIP = configManager.getMaxAccountsPerIP();
    }
}