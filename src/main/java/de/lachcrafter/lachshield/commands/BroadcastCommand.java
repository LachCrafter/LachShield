package de.lachcrafter.lachshield.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BroadcastCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("lachshield.admin")) {
            if (args.length == 0) {
                sender.sendMessage(miniMessage.deserialize("<red>Usage: /lachshield announcement <message>"));
            } else {
                String message = String.join(" ", args);

                String rawPrefix = config.getString("broadcast.prefix");
                String rawMessageColor = config.getString("broadcast.messagecolour");

                String fullMessage = rawPrefix + " " + rawMessageColor + message;

                Component broadcastMessage = miniMessage.deserialize(fullMessage);

                Bukkit.getServer().broadcast(broadcastMessage);
            }
            return true;
        } else {
            sender.sendMessage(miniMessage.deserialize("<red>You do not have permission to use this command!"));
        }
        return true;
    }
}
