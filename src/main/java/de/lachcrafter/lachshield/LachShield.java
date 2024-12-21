package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.BroadcastCommandOld;
import de.lachcrafter.lachshield.commands.LachShieldCommandOld;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.lib.Feature;
import de.lachcrafter.lachshield.lib.FeatureManager;
import de.lachcrafter.lachshield.managers.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class LachShield extends JavaPlugin {
    private ConfigManager configManager;
    private FeatureManager featureManager;
    public static final Logger LOGGER = LogManager.getLogger("LachSheld");

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Initialising LachShield...");

        this.configManager = new ConfigManager(this);

        LOGGER.info("Enabling Features...");
        enableFeatures();

        LOGGER.info("Registering Commands...");
        regCommands();

        int pluginId = 24143;
        Metrics metrics = new Metrics(this, pluginId);

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
        getCommand("lachshield").setExecutor(new LachShieldCommandOld(this));
        getCommand("broadcast").setExecutor(new BroadcastCommandOld(configManager));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }
}