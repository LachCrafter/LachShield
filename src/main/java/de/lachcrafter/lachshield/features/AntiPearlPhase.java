package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.lib.NewFeature;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class AntiPearlPhase extends NewFeature {

    private final LachShield plugin;

    public AntiPearlPhase(LachShield plugin) {
        super("AntiPearlPhase");
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && isPlayerLookingDown(player) &&
                isDestinationSafe(player.getLocation(), to)) {
            event.setCancelled(true);
            ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL, 1);
            player.getInventory().addItem(enderPearl);
        }
    }

    private boolean isPlayerLookingDown(Player player) {
        Location location = player.getLocation();
        double downPitch = plugin.getConfigManager().getConfig().getDouble("AntiPearlPhase.downPitch", 45.00D);
        double pitch = location.getPitch();
        return (pitch > downPitch || pitch < -downPitch);
    }

    private boolean isDestinationSafe(Location from, Location to) {
        double distance = from.distance(to);
        if (distance > 1.0D)
            return false;
        for (BlockFace face : BlockFace.values()) {
            if (face != BlockFace.DOWN && face != BlockFace.UP) {
                Location adjacent = to.clone().add(face.getModX(), 0.0D, face.getModZ());
                if (adjacent.getBlock().getType() != Material.AIR)
                    return true;
            }
        }
        return false;
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
    public void onReload() {}
}
