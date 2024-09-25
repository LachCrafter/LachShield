package de.lachcrafter.lachshield;

import com.github.retrooper.packetevents.PacketEvents;
import de.lachcrafter.lachshield.commands.BroadcastCommand;
import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class LachShield extends JavaPlugin {
    private final IPAccountManager ipAccountManager;
    private final PlayerObfuscator playerObfuscator;
    private final FileConfiguration config;
    private final ConfigManager configManager;

    private static final Logger LOGGER = LogManager.getLogger(LachShield.class);

    public LachShield
            (
                PlayerObfuscator playerObfuscator,
                FileConfiguration config,
                IPAccountManager ipAccountManager,
                ConfigManager configManager
            )
    {
        this.playerObfuscator = playerObfuscator;
        this.config = config;
        this.ipAccountManager = ipAccountManager;
        this.configManager = configManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        LOGGER.info("Initialising LachShield...");

        LOGGER.info("Registering Events...");
        regEvents();

        LOGGER.info("Registering Commands...");
        regCommands();

        LOGGER.info("LachShield successfully initialized");
    }


    // register events
    public void regEvents() {
        getServer().getPluginManager().registerEvents(new IPCheckListener(this, ipAccountManager), this);
        getServer().getPluginManager().registerEvents(new PreventNetherRoof(getConfig()), this);
        getServer().getPluginManager().registerEvents(new JoinMessages(getConfig()), this);
        getServer().getPluginManager().registerEvents(new Afk(this), this);

        PacketEvents.getAPI().getEventManager().registerListener(playerObfuscator);
    }

    // register commands
    public void regCommands() {
        getCommand("lachshield").setExecutor(new IPLimitCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(config));
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }
}
