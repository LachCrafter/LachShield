package de.lachcrafter.lachshield;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigManager {

    private final LachShield plugin;
    private final FileConfiguration config;

    public ConfigManager(LachShield plugin) {
        this.plugin = plugin;
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public int getMaxAccountsPerIP() {
        return config.getInt("max_accounts_per_ip", 3);
    }

    public String getKickMessage() {
        return config.getString("kick_message", "You are logged in with more than three accounts!");
    }
}