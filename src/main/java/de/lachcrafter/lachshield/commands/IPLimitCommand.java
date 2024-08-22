package de.lachcrafter.lachshield.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.lachcrafter.lachshield.LachShield;
import org.jetbrains.annotations.NotNull;

public class IPLimitCommand implements CommandExecutor {

    private final LachShield plugin;

    public IPLimitCommand(LachShield plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lachshield")) {
            if (args.length == 2 && args[0].equalsIgnoreCase("iplimit")) {
                if (sender instanceof Player player) {
                    if (player.hasPermission("lachshield.admin")) {
                        try {
                            int newLimit = Integer.parseInt(args[1]); 
                            plugin.getConfigManager().setMaxAccountsPerIP(newLimit);
                            player.sendMessage(Component.text("IP limit has been set to " + newLimit, NamedTextColor.GREEN));
                        } catch (NumberFormatException e) {
                            player.sendMessage(Component.text("Invalid number format. Usage: /lachshield iplimit <number>", NamedTextColor.RED));
                        }
                        return true;
                    } else {
                        player.sendMessage(Component.text("You don't have permission to use this command", NamedTextColor.RED));
                    }
                }
            }
        }
        return false;
    }
}
