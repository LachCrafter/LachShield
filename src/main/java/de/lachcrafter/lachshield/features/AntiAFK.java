package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class AntiAFK extends Feature {
    private final LachShield plugin = LachShield.plugin;

    private Map<Player, Long> playerTimestampMap;
    private ScheduledTask afkCheckTask;
    private final int timeoutInSeconds = LachShield.configManager.getConfig().getInt("AntiAFK.timeoutMinutes") * 60; // multiply to seconds

    public AntiAFK() {
        super("AntiAFK", true);
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        playerTimestampMap = new HashMap<>();

        afkCheckTask = plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, (_) -> playerTimestampMap.entrySet().iterator().forEachRemaining((entry) -> {

            if (getTimeDifferenceSeconds(entry.getValue()) >= timeoutInSeconds) {

                entry.getKey().kick(LachShield.configManager.getStringAsComponent("AntiAFK.kickMessage"));
                playerTimestampMap.remove(entry.getKey());
            }

        }), 20L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        if (!hasFeaturePermission(player)) {

            playerTimestampMap.put(player, currentTimeSeconds());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        var player = event.getPlayer();
        if (!hasFeaturePermission(player) || !LachShield.configManager.getConfig().getBoolean("AntiAFK.usePlayerInputEvent")) {

            playerTimestampMap.put(player, currentTimeSeconds());
        }
    }

    @EventHandler
    public void onPlayerInput(PlayerInputEvent event) {
        var player = event.getPlayer();
        if (!hasFeaturePermission(player) || LachShield.configManager.getConfig().getBoolean("AntiAFK.usePlayerInputEvent")) {
            playerTimestampMap.put(player, currentTimeSeconds());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        playerTimestampMap.remove(event.getPlayer());
    }

    private long getTimeDifferenceSeconds(long timestampSeconds) {

        return currentTimeSeconds() - timestampSeconds;
    }

    private long currentTimeSeconds() {

        return System.currentTimeMillis() / 1000; // divide to seconds
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        playerTimestampMap.clear();
        afkCheckTask.cancel();
    }

    @Override
    public void onReload() {
        playerTimestampMap.clear();

    }
}