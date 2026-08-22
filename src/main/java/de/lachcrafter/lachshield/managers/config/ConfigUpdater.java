package de.lachcrafter.lachshield.managers.config;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.util.FileUtil;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;

import java.io.*;
import java.util.*;

public class ConfigUpdater {
    private final FileConfiguration fileConfiguration;
    private final Plugin plugin;

    public static final int CONFIG_VERSION = 1;

    // Config is older than 1.9.
    private final boolean isPre1_9;
    // Config is older than 1.4.
    private final boolean isPre1_4;

    // Map of old config names of the features and their new ones.
    private final Map<String, String> legacyAndEquivalentNames_Pre_1_9 = new HashMap<>() {{
        put("ipLimit", "IPLimiter");
        put("preventNetherRoof", "AntiNetherRoof");
        put("afk", "AntiAFK");
        put("obfuscatePlayerData", "HidePlayerData");
        put("antiPearlPhase", "AntiPearlPhase");
        put("chatCensor", "ChatFilter");
    }};

    // List of removed config options, if the option has been available pre 1.9,
    // include its key from the old version.
    private final List<String> removedOptions = List.of(
            "obfuscatePlayerData.toObfuscate.onGround",
            "HidePlayerData.data.onGround"
    );

    public ConfigUpdater(Plugin plugin, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;

        isPre1_9 = fileConfiguration.isConfigurationSection("ipLimit");
        isPre1_4 = fileConfiguration.isInt("max_accounts_per_ip");

    }

    public void update() {
        LachShield.LOGGER.info("Old configuration detected. Updating the config...");
        final Map<String, Object> oldConfigValues = fileConfiguration.getValues(true);
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!generateBackupConfigFile(configFile)) {

            return;
        }

        plugin.saveResource("config.yml", true);
        plugin.reloadConfig();

        migrateConfiguration(oldConfigValues);
        LachShield.LOGGER.info("The configuration has been updated successfully.");
    }

    public void migrateConfiguration(Map<String, Object> oldConfigValues) {
        oldConfigValues.forEach((key, value) -> {
            if (value instanceof MemorySection || removedOptions.contains(key)) {

                return;
            }

            if (LachShield.configManager.getConfig().getBoolean("messages.verbose")) {
                LachShield.LOGGER.debug(key + " -> " + value);
            }

            if (isPre1_9) {

                if (legacyAndEquivalentNames_Pre_1_9.containsKey(key)) {
                    fileConfiguration.set(key.replace(key, legacyAndEquivalentNames_Pre_1_9.get(key)), value);
                }
            }

            else if (isPre1_4) {

                if (key.contains("max_accounts_per_ip")) {
                    fileConfiguration.set("maxAccountsPerIp", value);
                } if (key.contains("kick_message")) {
                    fileConfiguration.set("IPLimiter.kickMessage", value);
                }
            }

            else {
                plugin.getConfig().set(key, value);
            }
        });

        plugin.saveConfig();
    }

    /**
     * Generates the backup files with the content of the old config file.
     * <p>
     * <strong>Important: Rewrites old backup file.</strong>
     * @param configFile get the content of the old config file
     * @return False when an error occurred.
     */
    public boolean generateBackupConfigFile(@NonNull File configFile) {
        var backupFile = new File(plugin.getDataFolder(), "config.yml.bak");
        List<String> configContent = FileUtil.getFileContentAsList(configFile);
        if (backupFile.exists()) {

            if (!backupFile.delete()) {
                LachShield.LOGGER.error("An error occurred while deleting the old backup file." +
                        "\nThe update process has been cancelled to prevent data loss. Please open an issue on GitHub with the error message.");
                return false;
            }
        }

        if (configContent == null) {
            LachShield.LOGGER.error("An error occurred while generating the backup config file." +
                    "\nThe update process has been cancelled to prevent data loss. Please open an issue on GitHub with the error message.");
            return false;
        }

        try {

            if (!backupFile.createNewFile()) {
                LachShield.LOGGER.error("An error occurred while creating the new file for the config backup." +
                        "\nThe update process has been cancelled to prevent data loss. Please open an issue on GitHub with the error message.");
                return false;
            }
        } catch (IOException e) {
            LachShield.LOGGER.error("An error occurred while creating the new file for the config backup." +
                    "\nThe update process has been cancelled to prevent data loss. Please open an issue on GitHub with the error message and stack trace.");
            LachShield.LOGGER.error(e);
            return false;
        }

        if (!FileUtil.writeContentToFile(configContent.stream(), backupFile)) {
            LachShield.LOGGER.error("An error occurred while writing the old configuration content to the backup file." +
                    "\nThe update process has been cancelled to prevent data loss. Please open an issue on GitHub with the error message and stack trace.");
        }
        return true;
    }

    /**
     * Checks if the config file version doesn't match with the current one.
     * @return true if it doesn't match (and when the value doesn't exist) and false if it matches.
     */
    public boolean isUpdateNeeded() {
        return fileConfiguration.getInt("configVersion", 0) != CONFIG_VERSION;
    }
}