package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BroadcastCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("lachshield.admin")) {
            if (args.length == 0) {
                sender.sendMessage(miniMessage.deserialize("<red>Usage: /lachshield announcement <message>"));
            } else {
                String message = String.join(" ", args);

                String fullMessage = configManager.getBroadcastPrefix() + " " + configManager.getBroadcastMessageColor() + message;

                Component broadcastMessage = miniMessage.deserialize(fullMessage);

                Bukkit.getServer().broadcast(broadcastMessage);
            }
            return true;
        } else {
            sender.sendMessage(configManager.getNoPermission());
        }
        return true;
    }
}
