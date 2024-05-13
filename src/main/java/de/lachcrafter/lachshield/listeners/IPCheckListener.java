package de.lachcrafter.lachshield.listeners;

import de.lachcrafter.lachshield.LachShield;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IPCheckListener implements Listener {
    private final LachShield plugin;

    public IPCheckListener(LachShield plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("lachshield.admin")) {
            int ipLimit = plugin.getConfigManager().getMaxAccountsPerIP();
        }
    }
}
