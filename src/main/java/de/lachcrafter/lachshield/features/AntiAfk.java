package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.LachShield;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class AntiAfk extends Feature {
    private final LachShield plugin;
    private final ConfigManager configManager;
    private final HashMap<UUID, Long> playerActivity = new HashMap<>();
    private long afkTimeoutMinutes;
    private Component kickMessage;

    private int taskId = -1;

    public AntiAfk(LachShield plugin, ConfigManager configManager) {
        super("AntiAFK", false);
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (hasFeaturePermission(player)) return;
        playerActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (hasFeaturePermission(event.getPlayer())) return;
        playerActivity.remove(event.getPlayer().getUniqueId());
    }

    private void startAfkCheck() {
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID playerId = player.getUniqueId();

                    if (!playerActivity.containsKey(playerId)) {
                        playerActivity.put(playerId, currentTime);
                        continue;
                    }

                    long lastActivity = playerActivity.get(playerId);

                    if (currentTime - lastActivity > afkTimeoutMinutes) {
                        player.kick(kickMessage);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L, 20L * 60);
        taskId = runnable.getTaskId();
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        startAfkCheck();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTask(taskId);
        playerActivity.clear();
    }

    @Override
    public void onReload() {
        afkTimeoutMinutes = configManager.getAfkTimeoutMinutes() * 60 * 1000;
        kickMessage = configManager.getMessage("AntiAFK.kickMessage");
    }
}