package de.lachcrafter.lachshield.features;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessages implements Listener {

    private final FileConfiguration cfg;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public JoinMessages(FileConfiguration cfg) {
        this.cfg = cfg;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();

        if (!cfg.getBoolean("join_messages.enabled", false)) {
            return;
        }

        String joinMessage = cfg.getString("join_messages.join_message");
        if (joinMessage != null) {
            String formattedMessage = joinMessage.replace("%player%", player.getName());
            Component messageComponent = miniMessage.deserialize(formattedMessage);
            evt.joinMessage(messageComponent);
        }
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent evt) {
        if (!cfg.getBoolean("join_messages.enabled", false)) {
            return;
        }
        Player player = evt.getPlayer();

        String leaveMessage = cfg.getString("join_messages.leave_message");
        if (leaveMessage != null) {
            String formattedMessage = leaveMessage.replace("%player%", player.getName());
            Component messageComponent = miniMessage.deserialize(formattedMessage);
            evt.quitMessage(messageComponent);
        }
    }
}
