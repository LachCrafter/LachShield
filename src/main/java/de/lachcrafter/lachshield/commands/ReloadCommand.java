package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.ConfigManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final ConfigManager configManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ReloadCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("lachshield.admin")) {
            sender.sendMessage(configManager.getNoPermission());
            return true;
        }
        configManager.reloadConfig();
        sender.sendMessage(configManager.getReloadSuccessMessage());
        return true;
    }
}
