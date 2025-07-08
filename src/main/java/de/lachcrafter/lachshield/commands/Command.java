package de.lachcrafter.lachshield.commands;

import io.papermc.paper.command.brigadier.BasicCommand;

public abstract class Command implements BasicCommand {

    private final String alias;

    public Command(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

}
