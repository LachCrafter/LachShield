package de.lachcrafter.lachshield;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class LachShield extends JavaPlugin implements Listener {

    private Map<String, Integer> ipAccountCount = new HashMap<>();
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        int maxAccountsPerIP = configManager.getMaxAccountsPerIP();
        if (accountCount >= maxAccountsPerIP) {
            String kickMessage = configManager.getKickMessage();
            event.getPlayer().kickPlayer(ChatColor.RED + kickMessage);
            return;
        }

        ipAccountCount.put(ip, accountCount + 1);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount > 0) {
            ipAccountCount.put(ip, accountCount - 1);
        }
    }
}