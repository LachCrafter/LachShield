package de.lachcrafter.lachshield.managers;

import de.lachcrafter.lachshield.lib.Feature;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
        return config.getInt("ipLimit.maxAccountsPerIp", 3);
    }

    public void setMaxAccountsPerIP(int max) {
        config.set("ipLimit.maxAccountsPerIp", max);
        plugin.saveConfig();
    }

    public Component getNoPermission() {
        String rawMessage = config.getString("messages.noPermission", "<red>You don't have permission to execute this command!");
        return miniMessage.deserialize(rawMessage);
    }

    public Component getReloadSuccessMessage() {
        String rawMessage = config.getString("messages.reloadSuccess", "<green>The LachShield configuration has been successfully reloaded.");
        return miniMessage.deserialize(rawMessage);
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

    public Component getIpLimitKickMessage() {
        String rawMessage = config.getString("ipLimit.kickMessage", "<red>You have reached the account limit on the server!");
        return miniMessage.deserialize(rawMessage);
    }

    public Component getPreventNetherRoofWarningMessage() {
        String rawMessage = config.getString("preventNetherRoof.warnMessage", "<red>You cannot enter the Nether roof!");
        return miniMessage.deserialize(rawMessage);
    }

    public String getBroadcastFormat() {
        return config.getString("broadcast.format", "<prefix> <message_color><message>");
    }

    public Component getBroadcastPrefix() {
        String rawMessage = config.getString("broadcast.prefix", "<dark_aqua>[<red>Broadcast<dark_aqua>]");
        return miniMessage.deserialize(rawMessage);
    }

    public Component getBroadcastMessageColor() {
        String rawMessage = config.getString("broadcast.messageColor", "<gold>");
        return miniMessage.deserialize(rawMessage);
    }

    public long getAfkTimeoutMinutes() {
        return config.getInt("afk.timeoutMinutes", 15);
    }

    public Component getAfkKickMessage() {
        String rawMessage = config.getString("afk.kickMessage", "<red>You have been disconnected for AFK.");
        return miniMessage.deserialize(rawMessage);
    }
}