package de.lachcrafter.lachshield;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public int getMaxAccountsPerIP() {
        return config.getInt("max_accounts_per_ip", 3); // Default value is 3
    }

    public void setMaxAccountsPerIP(int max) {
        config.set("max_accounts_per_ip", max);
        plugin.saveConfig();
    }

    public String getKickMessage() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("kick_message", "&4You have too many accounts on the server!"));
    }

    public void setKickMessage(String message) {
        config.set("kick_message", message);
        plugin.saveConfig();
    }

    // New settings for enabling/disabling crystal break delay
    public boolean isCrystalDelayEnabled() {
        return config.getBoolean("crystal-delay", false); // Default value is false
    }

    public void setCrystalDelayEnabled(boolean enabled) {
        config.set("crystal-delay", enabled);
        plugin.saveConfig();
    }
}
