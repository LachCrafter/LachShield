package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.LachShieldCommand;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.lib.Feature;
import de.lachcrafter.lachshield.lib.FeatureManager;
import de.lachcrafter.lachshield.managers.ConfigManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LachShield extends JavaPlugin {
    public static ConfigManager configManager;
    private FeatureManager featureManager;
    public static final Logger LOGGER = LogManager.getLogger("LachShield");

    @Override
    public void onEnable() {
        LOGGER.info("Initialising LachShield...");

        configManager = new ConfigManager(this);

        LOGGER.info("Enabling Features...");
        enableFeatures();

        LOGGER.info("Registering Commands...");
        regCommands();

        LOGGER.info("LachShield successfully initialized.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }

    // Enable features
    public void enableFeatures() {
        List<Feature> features = new ArrayList<>(List.of(
                new IPAccountManager(this, configManager),
                new AntiNetherRoof(this, configManager),
                new ChatFilter(this)
        ));

        if (!isFolia()) {
            features.add(new AntiAfk(this, configManager));
            features.add(new AntiPearlPhase(this));
        }

        if (getServer().getPluginManager().isPluginEnabled("packetevents")) {
            features.add(new HidePlayerData(configManager));
        }

        featureManager = new FeatureManager(configManager);
        features.forEach(featureManager::register);
        featureManager.load();
    }

    // Register commands
    public void regCommands() {
        LifecycleEventManager<@NotNull Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register("lachshield", new LachShieldCommand(this));
        });
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    /**
     * Check if the server runs Folia
     *
     * @return true if Folia and false if else.
     */
    private static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}