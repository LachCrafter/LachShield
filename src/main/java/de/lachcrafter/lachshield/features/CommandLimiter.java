package de.lachcrafter.lachshield.features;

import de.lachcrafter.lachshield.LachShield;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.*;

public class CommandLimiter extends Feature {
    private final Map<UUID, String> inDelay = new HashMap<>();
    private final LachShield plugin;

    private List<String> coolDownCommands;

    public CommandLimiter(LachShield plugin) {
        super("CommandLimiter", true);
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChat(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);

        if (inDelay.containsKey(event.getPlayer().getUniqueId()) && inDelay.containsValue(command)) {
            event.getPlayer().sendRichMessage(plugin.getConfig().getString("CommandLimiter.message", "<red>Hey, stop spamming please."));
            event.setCancelled(true);
            return;
        }

        if (coolDownCommands.contains(command)) {
            doPlayerCooldown(event.getPlayer().getUniqueId(), command);
        }
    }

    public void doPlayerCooldown(UUID uuid, String command) {
        int cooldown = plugin.getConfig().getInt("CommandLimiter.cooldown", 100);

        inDelay.put(uuid, command);
        LachShield.schedulerFactory.scheduleInTicks(cooldown, () -> inDelay.remove(uuid));
    }

    @Override
    public void onEnable() {
        coolDownCommands = new ArrayList<>();
        coolDownCommands.addAll(plugin.getConfig().getStringList("CommandLimiter.commands"));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onReload() {
        inDelay.clear();
    }
}