package de.lachcrafter.lachshield.managers;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.commands.lachshield.LachShieldCommand;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CommandManager {
    private final LachShield plugin;

    public static final Component PREFIX = MiniMessage.miniMessage().deserialize("<gray>[<gold>LachShield</gold>]<reset> ");

    public CommandManager(LachShield plugin) {
        this.plugin = plugin;

        registerCommands();
    }

    public void registerCommands() {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(LachShieldCommand.createCommand());
        });
    }

}
