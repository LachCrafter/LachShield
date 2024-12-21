package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.managers.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommandOld implements CommandExecutor {
    private final ConfigManager configManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BroadcastCommandOld(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("lachshield.admin")) {
            if (args.length == 0) {
                sender.sendMessage(miniMessage.deserialize("<red>Usage: /broadcast <message>"));
            } else {
                String message = String.join(" ", args);

                Component broadcastMessage = miniMessage.deserialize(
                    configManager.getBroadcastFormat(),
                    Placeholder.component("prefix", configManager.getBroadcastPrefix()),
                    Placeholder.component("message_color", configManager.getBroadcastMessageColor()),
                    Placeholder.component("message", Component.text(message))
                );

                Bukkit.getServer().broadcast(broadcastMessage);
            }
        } else {
            sender.sendMessage(configManager.getNoPermission());
        }
        return true;
    }
}