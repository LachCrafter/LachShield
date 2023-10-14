package de.lachcrafter.lachshield;

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

        // Überprüfen, ob der Spieler die lachshield.admin-Berechtigung hat
        if (!player.hasPermission("lachshield.admin")) {
            int ipLimit = plugin.getConfigManager().getMaxAccountsPerIP();
            // Hier kannst du die IP-Überprüfung mit ipLimit durchführen
            // Wenn der Spieler zu viele Accounts mit derselben IP hat, kannst du ihn kicken.
            // Andernfalls kannst du ihn normal auf den Server lassen.
        }
    }
}
