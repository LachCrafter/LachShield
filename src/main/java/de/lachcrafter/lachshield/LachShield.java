package de.lachcrafter.lachshield;

import de.lachcrafter.lachshield.commands.BroadcastCommand;
import de.lachcrafter.lachshield.commands.IPLimitCommand;
import de.lachcrafter.lachshield.functions.IPAccountManager;
import de.lachcrafter.lachshield.functions.JoinMessages;
import de.lachcrafter.lachshield.functions.PreventNetherRoof;
import de.lachcrafter.lachshield.listeners.IPCheckListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LachShield extends JavaPlugin implements Listener {
    private ConfigManager configManager;
    private IPAccountManager ipAccountManager;
    private final FileConfiguration config;

    // Initialize the logger
    private static final Logger LOGGER = LogManager.getLogger(LachShield.class);

    public LachShield(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public void onEnable() {
        LOGGER.info("Initializing LachShield...");

        configManager = new ConfigManager(this);
        ipAccountManager = new IPAccountManager(configManager, config);

        LOGGER.info("Loading events...");
        // register events
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new IPCheckListener(this), this);
        getServer().getPluginManager().registerEvents(new PreventNetherRoof(getConfig()), this);
        getServer().getPluginManager().registerEvents(new JoinMessages(getConfig()), this);

        LOGGER.info("Loading commands...");
        // register commands
        getCommand("lachshield").setExecutor(new IPLimitCommand(this));
        getCommand("broadcast").setExecutor(new BroadcastCommand(config));

        LOGGER.info("LachShield successfully initialized");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public IPAccountManager getIPAccountManager() {
        return ipAccountManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!ipAccountManager.handlePlayerJoin(event.getPlayer())) {
            return;
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ipAccountManager.handlePlayerQuit(event.getPlayer());
    }

    @Override
    public void onDisable() {
        LOGGER.info("LachShield successfully unloaded");
    }
}
