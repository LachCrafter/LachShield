package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.functions.IPAccountManager;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LachShield extends JavaPlugin implements Listener {
    private ConfigManager configManager;
    private IPAccountManager ipAccountManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        ipAccountManager = new IPAccountManager(configManager);

        // Register existing listeners and commands
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new IPCheckListener(this), this);
        getCommand("lachshield").setExecutor(new IPLimitCommand(this));

        getLogger().info("LachShield loaded");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public IPAccountManager getIPAccountManager() {
        return ipAccountManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ipAccountManager.handlePlayerJoin(event.getPlayer())) {
            return;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ipAccountManager.handlePlayerQuit(event.getPlayer());
    }

    @Override
    public void onDisable() {
        getLogger().info("LachShield deactivated");
    }
}
