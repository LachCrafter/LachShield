package de.lachcrafter.lachshield.functions;

import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class PreventNetherRoof implements Listener {

    private final FileConfiguration config;

    public PreventNetherRoof(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!config.getBoolean("prevent_nether_roof.enabled", false)) {
            return;
        }

        if (world.getEnvironment() == World.Environment.NETHER && player.getLocation().getY() >= 128) {
            event.setCancelled(true);

            String warningMessage = config.getString("prevent_nether_roof.warning_message", "<red>You cannot enter the Nether roof!");

            player.sendMessage(Component.text(warningMessage));

            player.teleport(player.getLocation().set(player.getLocation().getX(), 127, player.getLocation().getZ()));
        }
    }
}
