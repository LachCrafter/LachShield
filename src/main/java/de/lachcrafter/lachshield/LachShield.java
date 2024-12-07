package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.BroadcastCommand;
import de.lachcrafter.lachshield.commands.LachShieldCommand;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.lib.Feature;
import de.lachcrafter.lachshield.lib.FeatureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LachShield extends JavaPlugin {
    private ConfigManager configManager;
    private FeatureManager featureManager;
    public static final Logger LOGGER = LogManager.getLogger(LachShield.class);

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Initialising LachShield...");

        this.configManager = new ConfigManager(this);

        LOGGER.info("Enabling Features...");
        enableFeatures();

        LOGGER.info("Registering Commands...");
        regCommands();

        LOGGER.info("LachShield successfully initialized in {}ms", System.currentTimeMillis() - startTime);
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }

    // Enable features
    public void enableFeatures() {
        List<Feature> features = new ArrayList<>(List.of(
                new AntiAfk(this, configManager),
                new IPAccountManager(this, configManager),
                new PreventNetherRoof(this, configManager)
        ));

        if (getServer().getPluginManager().isPluginEnabled("packetevents")) {
            features.add(new PlayerObfuscator(configManager));
        }

        featureManager = new FeatureManager(configManager);
        features.forEach(featureManager::register);
        featureManager.load();
    }

    // Register commands
    public void regCommands() {
        getCommand("lachshield").setExecutor(new LachShieldCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(configManager));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }
}