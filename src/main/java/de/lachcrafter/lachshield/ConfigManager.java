package de.lachcrafter.lachshield;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public int getMaxAccountsPerIP() {
        return config.getInt("ip_limit.maxAccountsPerIp", 3);
    }

    public void setMaxAccountsPerIP(int max) {
        config.set("ip_limit:max_accounts_per_ip", max);
        plugin.saveConfig();
    }

    public Component getKickMessage() {
        String rawMessage = config.getString("ip_limit.kickMessage", "<red>You have too many accounts on the server!");
        return miniMessage.deserialize(rawMessage);
    }
}
