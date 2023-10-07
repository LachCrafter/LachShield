package de.lachcrafter.lachshield.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.lachcrafter.lachshield.LachShield;

public class IPLimitCommand implements CommandExecutor {

    private final LachShield plugin;

    public IPLimitCommand(LachShield plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lachshield")) {
            if (args.length == 2 && args[0].equalsIgnoreCase("iplimit")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("lachshield.admin")) {
                        try {
                            int newLimit = Integer.parseInt(args[1]);
                            plugin.getConfigManager().setMaxAccountsPerIP(newLimit);
                            player.sendMessage(ChatColor.GREEN + "IP limit set to " + newLimit);
                        } catch (NumberFormatException e) {
                            player.sendMessage(ChatColor.RED + "Invalid number format. Usage: /lachshield iplimit <number>");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command.");
                }
            }
        }
        return false;
    }
}