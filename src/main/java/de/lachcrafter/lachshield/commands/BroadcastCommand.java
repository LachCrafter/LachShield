package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.managers.ConfigManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class BroadcastCommand implements BasicCommand {
    private final ConfigManager configManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BroadcastCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void execute(CommandSourceStack stack, String @NotNull [] args) {

        if (!stack.getSender().hasPermission("lachshield.admin")) {
            stack.getSender().sendMessage(configManager.getNoPermission());
            return;
        }

        String message = String.join(" ", args);

        if (message.trim().isEmpty()) {
            stack.getSender().sendMessage(miniMessage.deserialize("<red>Usage: /broadcast <message>"));
            return;
        }

        Component broadcastMessage = miniMessage.deserialize(
                configManager.getBroadcastFormat(),
                Placeholder.component("prefix", configManager.getBroadcastPrefix()),
                Placeholder.component("message_color", configManager.getBroadcastMessageColor()),
                Placeholder.component("message", Component.text(message))
        );

        Bukkit.getServer().broadcast(broadcastMessage);
    }
}
