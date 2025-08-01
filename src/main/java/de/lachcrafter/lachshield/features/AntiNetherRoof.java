package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.LachShield;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.vehicle.VehicleCreateEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AntiNetherRoof extends Feature {
    private final LachShield plugin;
    private final ConfigManager configManager;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private Component antiNetherRoofWarningMessage;

    public AntiNetherRoof(LachShield plugin, ConfigManager configManager) {
        super("AntiNetherRoof", true);
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (hasFeaturePermission(player)) return;

        if (world.getEnvironment() == World.Environment.NETHER && player.getLocation().getY() >= 128) {
            if (isInCooldown(player)) {
                return;
            }
            setCooldown(player);

            player.sendMessage(antiNetherRoofWarningMessage);

            Location safeLocation = findSafeLocation(player.getLocation());
            if (safeLocation == null) {
                Location headLocation = player.getLocation().add(0, -7, 0);
                Location feetLocation = player.getLocation().add(0, -8, 0);

                if (headLocation.getBlock().getBlockData().getMaterial() == Material.NETHERRACK && feetLocation.getBlock().getBlockData().getMaterial() == Material.NETHERRACK) {
                    headLocation.getBlock().breakNaturally();
                    feetLocation.getBlock().breakNaturally();
                }
                player.teleportAsync(feetLocation);
                return;
            }

            player.teleportAsync(Objects.requireNonNullElseGet(safeLocation, () -> new Location(world, player.getLocation().getX(), 124, player.getLocation().getZ())));
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Location location = event.getLocation();
        Entity entity = event.getEntity();
        World world = location.getWorld();

        if (world.getEnvironment() == World.Environment.NETHER && location.getBlockY() >= 126 && entity instanceof EnderPearl) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location location = event.getBlock().getLocation();
        World world = location.getWorld();

        if (world.getEnvironment() == World.Environment.NETHER && location.getBlockY() >= 128 && hasFeaturePermission(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        Location location = event.getVehicle().getLocation();
        World world = location.getWorld();

        if (world.getEnvironment() == World.Environment.NETHER && location.getBlockY() >= 128) {
            event.setCancelled(true);
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

        for (int y = 121; y >= 115; y--) {
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

    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        cooldowns.clear();
    }

    @Override
    public void onReload() {
        antiNetherRoofWarningMessage = configManager.getMessage("AntiNetherRoof.warnMessage");
    }
}