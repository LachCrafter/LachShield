package de.lachcrafter.lachshield.managers;

import de.lachcrafter.lachshield.lib.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Get and set values from the config.yml
 *
 * @since 1.6
 */
public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    /**
     * load the configuration
     */
    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    /**
     * reload the configuration
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    /**
     * Get the configuration file
     */
    public FileConfiguration getConfig() {
        return config;
    }

    public int getMaxAccountsPerIP() {
        return config.getInt("IPLimiter.maxAccountsPerIp", 3);
    }

    public void setMaxAccountsPerIP(int max) {
        config.set("IPLimiter.maxAccountsPerIp", max);
        plugin.saveConfig();
    }

    public Component getMessage(String path) {
        return miniMessage.deserialize(config.getString(path, "Message " + path + " not found."));
    }

    public @NotNull List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public boolean isFeatureEnabled(String feature) {
        return config.getBoolean(feature + ".enabled", true);
    }

    public void setFeatureEnabled(String feature, boolean enabled) {
        config.set(feature + ".enabled", enabled);
        plugin.saveConfig();
    }

    public void setFeatureEnabled(Feature feature, boolean enabled) {
        setFeatureEnabled(feature.getFeatureName(), enabled);
    }

    public long getAfkTimeoutMinutes() {
        return config.getInt("AntiAFK.timeoutMinutes", 15);
    }

    public List<String> getChatBlacklist() {
        return config.getStringList("ChatFilter.blacklist");
    }
}