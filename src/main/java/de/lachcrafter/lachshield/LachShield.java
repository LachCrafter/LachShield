package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.LachShieldCommand;
import de.lachcrafter.lachshield.lib.NewFeatureManager;
import de.lachcrafter.lachshield.managers.ConfigManager;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LachShield extends JavaPlugin {
    public static ConfigManager configManager;
    public static NewFeatureManager featureManager;
    public static final Logger LOGGER = LogManager.getLogger("LachShield");

    @Override
    public void onEnable() {
        LOGGER.info("Initialising LachShield...");

        configManager = new ConfigManager(this);

        LOGGER.info("Loading Features...");
        featureManager = new NewFeatureManager(this);

        LOGGER.info("Registering Commands...");
        regCommands();

        LOGGER.info("LachShield successfully initialized.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
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

    /**
     * Check if the server runs Folia
     *
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