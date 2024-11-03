package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.LachShield;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LachShield main;

    public ReloadCommand(FileConfiguration config, LachShield main) {
        this.config = config;
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("lachshield.admin")) {
            Component noPermission = miniMessage.deserialize(config.getString("no-permission"));
            sender.sendMessage(noPermission);
            return true;
        }
        main.reloadConfig();
        Component reloadSuccess = miniMessage.deserialize(config.getString("reload-success"));
        sender.sendMessage(reloadSuccess);
        return true;
    }
}
