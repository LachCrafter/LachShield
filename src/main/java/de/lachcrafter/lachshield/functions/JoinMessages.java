package de.lachcrafter.lachshield.functions;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessages implements Listener {

    private final FileConfiguration cfg;

    public JoinMessages(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();

        if (!cfg.getBoolean("join_messages.enabled", false)) {
            return;
        }

        Bukkit.getServer().broadcast(Component.text(player.getName() + " " + cfg.getString("join_messages.join_message")));
    }

    public void onPlayerLeaveEvent(PlayerQuitEvent evt) {
        if (!cfg.getBoolean("join_messages.enabled", false)) {
            return;
        }
        Player player = evt.getPlayer();

        Bukkit.getServer().broadcast(Component.text(player.getName() + " "+ cfg.getString("join_message.leave_message")));
    }
}
