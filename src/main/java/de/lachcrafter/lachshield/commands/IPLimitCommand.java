package de.lachcrafter.lachshield.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import de.lachcrafter.lachshield.LachShield;
import org.jetbrains.annotations.NotNull;

public class IPLimitCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final LachShield plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public IPLimitCommand(LachShield plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lachshield")) {
            if (args.length == 2 && args[0].equalsIgnoreCase("iplimit")) {
                if (sender instanceof Player player) {
                    if (player.hasPermission("lachshield.admin")) {
                        try {
                            int newLimit = Integer.parseInt(args[1]);
                            if (newLimit < 1) {
                                player.sendMessage(Component.text("The ip limit should be 1 or more", NamedTextColor.RED));
                                return true;
                            }
                            plugin.getConfigManager().setMaxAccountsPerIP(newLimit);
                            player.sendMessage(Component.text("IP limit has been set to " + newLimit, NamedTextColor.GREEN));
                        } catch (NumberFormatException e) {
                            player.sendMessage(Component.text("Invalid number format. Usage: /lachshield iplimit <number>", NamedTextColor.RED));
                        }
                        return true;
                    } else {
                        Component noPermission = miniMessage.deserialize(config.getString("messages.noPermission"));
                        player.sendMessage(noPermission);
                    }
                }
            }
        }
        return true;
    }
}
