package de.lachcrafter.lachshield.functions;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;

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

        if (player.hasPermission("lachshield.admin")) {
            return;
        }

        if (world.getEnvironment() == World.Environment.NETHER && player.getLocation().getY() >= 128) {
            event.setCancelled(true);

            String warningMessage = config.getString("prevent_nether_roof.warning_message", "<red>You cannot enter the Nether roof!");
            player.sendMessage(MiniMessage.miniMessage().deserialize(warningMessage));

            Location safeLocation = player.getLocation();
            safeLocation.setY(127);

            if (world.getBlockAt(safeLocation).getType() == Material.AIR && world.getBlockAt(safeLocation.add(0, 1, 0)).getType() == Material.AIR) {
                player.teleport(safeLocation);
            } else {
                safeLocation.setY(126);
                player.teleport(safeLocation);
            }
        }
    }
}
