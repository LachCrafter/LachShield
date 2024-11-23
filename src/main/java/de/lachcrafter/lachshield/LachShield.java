package de.lachcrafter.lachshield;

import com.github.retrooper.packetevents.PacketEvents;
import de.lachcrafter.lachshield.commands.BroadcastCommand;
import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.commands.ReloadCommand;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LachShield extends JavaPlugin {
    private IPAccountManager ipAccountManager;
    private PlayerObfuscator playerObfuscator;
    private FileConfiguration config;
    private ConfigManager configManager;

    private static final Logger LOGGER = LogManager.getLogger(LachShield.class);

    public LachShield() {
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        LOGGER.info("Initialising LachShield...");

        this.config = getConfig();
        this.configManager = new ConfigManager(this);
        this.ipAccountManager = new IPAccountManager(configManager, config);
        this.playerObfuscator = new PlayerObfuscator();

        LOGGER.info("Registering Events...");
        regEvents();

        LOGGER.info("Registering Commands...");
        regCommands();

        LOGGER.info("LachShield successfully initialized");
    }

    // Register events
    public void regEvents() {
        getServer().getPluginManager().registerEvents(new IPCheckListener(this, ipAccountManager), this);
        getServer().getPluginManager().registerEvents(new PreventNetherRoof(getConfig()), this);
        getServer().getPluginManager().registerEvents(new Afk(this), this);

        PacketEvents.getAPI().getEventManager().registerListener(playerObfuscator);
    }

    // Register commands
    public void regCommands() {
        getCommand("lachshield").setExecutor(new IPLimitCommand(this, config));
        getCommand("broadcast").setExecutor(new BroadcastCommand(config));
        getCommand("lachshield-reload").setExecutor(new ReloadCommand(config, this));
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }
}
