package de.lachcrafter.lachshield.functions;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.UUID;

public class CrystalDelay {
    private final HashMap<UUID, Long> lastBreakAttempt = new HashMap<>();
    private final ConfigManager configManager;

    public CrystalDelay(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        if (!configManager.isCrystalDelayEnabled()) {
            return; // Early exit if the delay feature is disabled
        }

        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (block.getType() == Material.END_CRYSTAL) {
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();
            long delayMs = 10000; // The fixed delay if the feature is enabled

            if (lastBreakAttempt.containsKey(playerId) &&
                    (currentTime - lastBreakAttempt.get(playerId) < delayMs)) {
                event.setCancelled(true);
                player.sendMessage("§cYou must wait " + (delayMs / 1000) + " seconds before breaking another end crystal!");
            } else {
                lastBreakAttempt.put(playerId, currentTime);
            }
        }
    }
}
