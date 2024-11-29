package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.ConfigManager;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PreventNetherRoof implements Listener {

    private final FileConfiguration config;
    private ConfigManager configManager;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

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
            if (isInCooldown(player)) {
                return;
            }
            setCooldown(player);

            player.sendMessage(configManager.getPreventNetherRoofWarningMessage());

            Location safeLocation = findSafeLocation(player.getLocation());

            player.teleport(Objects.requireNonNullElseGet(safeLocation, () -> new Location(world, player.getLocation().getX(), 124, player.getLocation().getZ())));
        }
    }

    private boolean isInCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (cooldowns.containsKey(playerId)) {
            long lastTime = cooldowns.get(playerId);
            return (System.currentTimeMillis() - lastTime) < 2000;
        }
        return false;
    }

    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private Location findSafeLocation(Location originalLocation) {
        World world = originalLocation.getWorld();
        if (world == null) return null;

        for (int y = 127; y >= 124; y--) {
            Location checkLocation = new Location(world, originalLocation.getX(), y, originalLocation.getZ());
            if (isSafeLocation(checkLocation)) {
                return checkLocation;
            }
        }

        return null;
    }

    private boolean isSafeLocation(Location location) {
        World world = location.getWorld();
        if (world == null) return false;

        Location feetLocation = location.clone();
        Location headLocation = location.clone().add(0, 1, 0);

        Material blockAtFeet = world.getBlockAt(feetLocation).getType();
        Material blockAtHead = world.getBlockAt(headLocation).getType();

        return blockAtFeet == Material.AIR && blockAtHead == Material.AIR;
    }
}
