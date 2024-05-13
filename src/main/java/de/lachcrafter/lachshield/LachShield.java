package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.functions.CrystalDelay;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import de.lachcrafter.lachshield.listeners.CrystalListener;
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
    private CrystalDelay crystalDelay;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        crystalDelay = new CrystalDelay(configManager);  // Pass the configManager to CrystalDelay

        // Register existing listeners and commands
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new IPCheckListener(this), this);
        getServer().getPluginManager().registerEvents(new CrystalListener(crystalDelay), this);
        getCommand("lachshield").setExecutor(new IPLimitCommand(this));

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
