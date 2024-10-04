package de.lachcrafter.lachshield.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements CommandExecutor {

    private final FileConfiguration config;

    public BroadcastCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("lachshield.admin")) {
            if (args.length == 0) {
                sender.sendMessage(Component.text("Usage: /lachshield announcement <message>"));
            } else {
                String message = String.join(" ", args);
                Bukkit.getServer().broadcast(Component.text(config.getString("broadcast.prefix") + " " + config.getString("broadcast.messagecolour") + message));
            }
            return true;
        } else {
            sender.sendMessage(Component.text("You do not have permission to use this command!"));
        } return true;
    }
}
