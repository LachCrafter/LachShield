package de.lachcrafter.lachshield.managers;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.commands.Command;
import de.lachcrafter.lachshield.commands.LachShieldCommand;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final LachShield plugin;

    public final List<Command> commandsList;

    public CommandManager(LachShield plugin) {
        this.plugin = plugin;

        // List all commands here
        commandsList = new ArrayList<>(List.of(
                new LachShieldCommand(plugin)
        ));

        // Initialize and register the commands.
        initializeCommands();

    }

    public void initializeCommands() {
        LifecycleEventManager<@NotNull Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commandsList.forEach(command -> commands.register(command.getAlias(), command));
        });
    }

}
