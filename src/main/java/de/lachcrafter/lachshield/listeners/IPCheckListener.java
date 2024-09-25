package de.lachcrafter.lachshield.listeners;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.features.IPAccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IPCheckListener implements Listener {
    private final LachShield plugin;
    private IPAccountManager ipAccountManager;

    public IPCheckListener(LachShield plugin, IPAccountManager ipAccountManager) {
        this.plugin = plugin;
        this.ipAccountManager = ipAccountManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("lachshield.admin")) {
            int ipLimit = plugin.getConfigManager().getMaxAccountsPerIP();
        }

        ipAccountManager.handlePlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("lachshield.admin")) {
            int ipLimit = plugin.getConfigManager().getMaxAccountsPerIP();
        }

        ipAccountManager.handlePlayerQuit(event.getPlayer());
    }

}
