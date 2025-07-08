package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.managers.CommandManager;
import de.lachcrafter.lachshield.managers.FeatureManager;
import de.lachcrafter.lachshield.managers.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class LachShield extends JavaPlugin {
    public static ConfigManager configManager;
    public static FeatureManager featureManager;
    public static CommandManager commandManager;
    public static final Logger LOGGER = LogManager.getLogger("LachShield");

    @Override
    public void onEnable() {
        LOGGER.info("Initialising LachShield...");

        configManager = new ConfigManager(this);

        LOGGER.info("Loading Features...");
        featureManager = new FeatureManager(this);

        LOGGER.info("Registering Commands...");
        commandManager = new CommandManager(this);

        LOGGER.info("LachShield successfully initialized.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }

    /**
     * Check if the server runs Folia
     * @return true if Folia and false if else.
     */
    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}