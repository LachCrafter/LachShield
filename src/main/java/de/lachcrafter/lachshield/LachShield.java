package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class LachShield extends JavaPlugin implements Listener {
    private final Map<String, Integer> ipAccountCount = new HashMap<>();
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        // Register existing listeners and commands
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new IPCheckListener(this), this);
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
            Component kickComponent = configManager.getKickMessage();
            event.getPlayer().kick(kickComponent);
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
