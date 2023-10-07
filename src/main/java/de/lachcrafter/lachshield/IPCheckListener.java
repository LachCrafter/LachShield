package de.lachcrafter.lachshield;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class IPCheckListener implements Listener {

    private final LachShield plugin;

    public IPCheckListener(LachShield plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String ipAddress = event.getAddress().getHostAddress();
        int maxAccountsPerIP = plugin.getConfigManager().getMaxAccountsPerIP();

        int accountsWithSameIP = plugin.getPlayerAccountsCount(ipAddress);

        if (accountsWithSameIP >= maxAccountsPerIP) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, plugin.getConfigManager().getKickMessage());
        }
    }
}