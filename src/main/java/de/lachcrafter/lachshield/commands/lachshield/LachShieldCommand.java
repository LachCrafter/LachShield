package de.lachcrafter.lachshield.commands.lachshield;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.managers.CommandManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import java.util.ArrayList;
import java.util.List;

public class LachShieldCommand {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("lachshield")
                .requires(ctx -> (ctx.getSender().hasPermission("lachshield.admin") || ctx.getSender().hasPermission("lachshuield.commands")))
                .executes(LachShieldCommand::executeHelpMessage)

                .then(Commands.literal("status").executes(LachShieldCommand::executeStatusMessage))
                .then(Commands.literal("enable").then(Commands.argument("feature", StringArgumentType.word())
                        .suggests((_, builder) -> {
                            LachShield.featureManager.getDisabledFeatures().forEach(feature -> builder.suggest(feature.getName()));
                            return builder.buildFuture();
                        })
                        .executes(LachShieldCommand::executeEnableFeature))
                )

                .then(Commands.literal("disable").then(Commands.argument("feature", StringArgumentType.word())
                        .suggests((_, builder) -> {
                            LachShield.featureManager.getEnabledFeatures().forEach(feature -> builder.suggest(feature.getName()));
                            return builder.buildFuture();
                        })
                        .executes(LachShieldCommand::executeDisableFeature))
                )


                .then(ReloadSubCommand.createCommand())

                .build();
    }

    private static int executeHelpMessage(CommandContext<CommandSourceStack> ctx) {
        TextComponent message = Component.text()
                .append(CommandManager.PREFIX)
                .append(Component.text("LachShield v" + LachShield.plugin.getPluginMeta().getVersion() + " - sub-commands", NamedTextColor.GOLD))
                .appendNewline()
                .append(Component.text("- /lachshield reload <config|all|feature> - reloads a specific feature or everything"))
                .appendNewline()
                .append(Component.text("- /lachshield enable <feature> - enables a specific feature"))
                .appendNewline()
                .append(Component.text("- /lachshield disable <feature> - disables a specific feature"))
                .appendNewline()
                .append(Component.text("- /lachshield status - lists all enabled and disabled features"))
                .appendNewline()
                .append(Component.text("- /lachshield iplimit <number> - sets the player ip limit"))
                .color(NamedTextColor.DARK_GREEN)
                .build();

        ctx.getSource().getSender().sendMessage(message);
        return Command.SINGLE_SUCCESS;
    }

    private static int executeStatusMessage(CommandContext<CommandSourceStack> ctx) {
        List<Component> enabledFeatures = new ArrayList<>() {{
            LachShield.featureManager.getEnabledFeatures().forEach(f ->
                    add(Component.text("\n" + f.getName()).color(NamedTextColor.GREEN))
            );
        }};

        List<Component> disabledFeatures = new ArrayList<>() {{
            LachShield.featureManager.getDisabledFeatures().forEach(f ->

                    add(mm.deserialize("\n" + f.getName()).color(NamedTextColor.RED))
            );
        }};

        ctx.getSource().getSender().sendMessage(Component.text()
                .append(CommandManager.PREFIX)
                .append(Component.text("Feature status").color(NamedTextColor.GOLD))
                .appendNewline()

                .append(mm.deserialize("<gold>Enabled features (<yellow><amount></yellow>):",
                        Placeholder.unparsed("amount", String.valueOf(enabledFeatures.size()))))
                .append(enabledFeatures).appendNewline()

                .append(mm.deserialize("<gold>Disabled features (<yellow><amount></yellow>):",
                        Placeholder.unparsed("amount", String.valueOf(disabledFeatures.size()))))
                .append(disabledFeatures)

        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeEnableFeature(CommandContext<CommandSourceStack> ctx) {
        var featureString = ctx.getArgument("feature", String.class);
        var feature = LachShield.featureManager.getFeatureByName(featureString);

        if (feature.isEmpty()) {
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray>" + featureString + "</gray> does not exist.");
        } else if (LachShield.featureManager.getEnabledFeatures().contains(feature.get())) {
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray>" + featureString + "</gray> is already enabled.");
        } else {
            LachShield.featureManager.enableFeature(feature.get());
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <green>Feature <gray>" + featureString + "</gray> has been enabled.");
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int executeDisableFeature(CommandContext<CommandSourceStack> ctx) {
        var featureString = ctx.getArgument("feature", String.class);
        var feature = LachShield.featureManager.getFeatureByName(featureString);

        if (feature.isEmpty()) {
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray><feature></gray> does not exist.",
                    Placeholder.unparsed("feature", featureString));
        } else if (LachShield.featureManager.getDisabledFeatures().contains(feature.get())) {
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <red>Feature <gray><feature></gray> is already disabled.",
                    Placeholder.unparsed("feature", featureString));
        } else {
            LachShield.featureManager.disableFeature(feature.get());
            ctx.getSource().getSender().sendRichMessage("<gray>[<gold>LachShield</gold>] <green>Feature <gray><feature></gray> has been disabled.",
                    Placeholder.unparsed("feature", featureString));
        }

        return Command.SINGLE_SUCCESS;
    }
}