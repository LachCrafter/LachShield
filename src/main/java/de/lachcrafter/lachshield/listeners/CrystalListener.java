package de.lachcrafter.lachshield.listeners;

import de.lachcrafter.lachshield.functions.CrystalDelay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CrystalListener implements Listener {
    private CrystalDelay crystalDelay;

    public CrystalListener(CrystalDelay crystalDelay) {
        this.crystalDelay = crystalDelay;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        crystalDelay.handleBlockBreak(event);
    }
}
