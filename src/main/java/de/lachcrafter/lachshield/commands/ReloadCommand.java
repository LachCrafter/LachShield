package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.ConfigManager;
import de.lachcrafter.lachshield.LachShield;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private ConfigManager configManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LachShield main;

    public ReloadCommand(LachShield main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("lachshield.admin")) {
            sender.sendMessage(configManager.getNoPermission());
            return true;
        }
        main.reloadConfig();
        sender.sendMessage(configManager.getReloadSuccessMessage());
        return true;
    }
}
