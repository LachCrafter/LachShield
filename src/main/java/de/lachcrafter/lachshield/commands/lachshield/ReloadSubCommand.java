package de.lachcrafter.lachshield.commands.lachshield;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.lachcrafter.lachshield.LachShield;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class ReloadSubCommand {

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("reload")
                .then(Commands.literal("full")
                        .executes(ctx -> {
                            LachShield.configManager.reloadConfig();
                            LachShield.featureManager.getEnabledFeatures().forEach(LachShield.featureManager::reloadFeature);
                            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] Every feature and the configuration have been successfully reloaded.");

                            return Command.SINGLE_SUCCESS;
                        })
                )

                .then(Commands.literal("config")
                        .executes(ctx ->   {
                            LachShield.configManager.reloadConfig();
                            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <green>The configuration has been successfully reloaded.");

                            return Command.SINGLE_SUCCESS;
                        })
                )

                .then(Commands.literal("feature").then(Commands.argument("feature", StringArgumentType.word())

                        .suggests((_, builder) -> {
                            LachShield.featureManager.getEnabledFeatures().forEach(feature -> builder.suggest(feature.getName()));
                            return builder.buildFuture();
                        })
                        .executes(ctx -> {
                            var featureString = ctx.getArgument("feature", String.class);
                            var feature = LachShield.featureManager.getFeatureByName(featureString);

                            if (feature.isEmpty()) {
                                ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray>" + featureString + "</gray> does not exist.");
                            } else if (LachShield.featureManager.getEnabledFeatures().contains(feature.get())) {
                                ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray>" + featureString + "</gray> is not enabled.");
                            } else {
                                LachShield.featureManager.reloadFeature(feature.get());
                                ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <green>Feature <gray>" + featureString + "</gray> has been successfully reloaded.");
                            }

                            return Command.SINGLE_SUCCESS;
                        }))
                ).build();
    }

}