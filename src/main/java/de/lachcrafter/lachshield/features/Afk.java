package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Afk implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> playerActivity = new HashMap<>();
    private final long afkTimeoutMinutes;
    private final Component kickMessage;
    private final boolean enabled;

    public Afk(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("afk.enabled");
        this.afkTimeoutMinutes = configManager.getAfkTimeoutMinutes() * 60 * 1000;

        this.kickMessage = configManager.getAfkKickMessage();

        if (enabled) {
            startAfkCheck();
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!enabled) return;
        if (player.hasPermission("lachshield.admin")) {
            return;
        }
        playerActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void startAfkCheck() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!enabled) return;
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
    }
}
