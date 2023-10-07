package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.IPLimitCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("lachshield").setExecutor(new IPLimitCommand(this));
        getServer().getPluginManager().registerEvents(new IPCheckListener(this), this);
        getLogger().info("LachShield loaded");
    }

    public ConfigManager getConfigManager() {
        return configManager;
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

    public void kickPlayerAndRemoveEntity(Player player, String kickMessage) {
        player.kickPlayer(kickMessage);
        player.remove();
    }

    public int getPlayerAccountsCount(String ipAddress) {
        int count = 0;
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getAddress().getHostString().equals(ipAddress)) {
                count++;
            }
        }
        return  count;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount > 0) {
            ipAccountCount.put(ip, accountCount - 1);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("LachShield deactivated");
    }
}