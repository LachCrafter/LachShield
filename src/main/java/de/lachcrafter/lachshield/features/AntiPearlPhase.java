package de.lachcrafter.lachshield.features;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import de.lachcrafter.lachshield.LachShield;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;

import java.util.List;

public class AntiPearlPhase extends Feature {
    private final LachShield plugin;
    private final List<Material> whitelist = List.of(Material.WATER, Material.SHORT_DRY_GRASS, Material.AIR, Material.TALL_GRASS, Material.SHORT_GRASS, Material.TALL_DRY_GRASS, Material.SEAGRASS, Material.TALL_SEAGRASS);

    public AntiPearlPhase(LachShield plugin) {
        super("AntiPearlPhase", true);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerItemInteract(PlayerLaunchProjectileEvent event) {
        var player = event.getPlayer();
        var projectile = event.getProjectile();
        var targetDistanceLimit = plugin.getConfig().getInt("AntiPearlPhaseRewrite.targetDistance", 3);
        var downPitchLimit = plugin.getConfig().getDouble("AntiPearlPhaseRewrite.downPitch", 45.00);

        if (projectile.getType() == EntityType.ENDER_PEARL
                && player.getTargetBlockExact(targetDistanceLimit) != null
                && player.getPitch() >= downPitchLimit
                && isPlayerLocationSurrounded(player.getLocation())
        ) {

            event.setCancelled(true);
        }
    }

    private boolean isPlayerLocationSurrounded(Location playerLocation) {

        for (Location checkLocation : getCheckLocations(playerLocation)) {
            if (!whitelist.contains(checkLocation.getBlock().getType())) {

                return true;
            }
        }

        return false;
    }

    private List<Location> getCheckLocations(Location playerLocation) {
        var playerWorld = playerLocation.getWorld();
        var playerX = playerLocation.getX();
        var playerY = playerLocation.getY();
        var playerZ = playerLocation.getZ();

        return List.of(
                new Location(playerWorld, playerX + 1, playerY, playerZ),   // positive X
                new Location(playerWorld, playerX - 1, playerY, playerZ),   // negative X
                new Location(playerWorld, playerX, playerY, playerZ + 1),   // positive Z
                new Location(playerWorld, playerX, playerY, playerZ - 1)    // negative Z
        );
    }

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onReload() {

    }
}
