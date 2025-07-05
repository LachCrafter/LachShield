package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.lib.Feature;
import de.lachcrafter.lachshield.lib.FeatureManager;
import de.lachcrafter.lachshield.managers.ConfigManager;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class LachShieldCommand implements BasicCommand {

    private final LachShield plugin;
    private final ConfigManager configManager;
    private final FeatureManager featureManager;

    public LachShieldCommand(LachShield plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.featureManager = plugin.getFeatureManager();
    }

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String @NotNull [] args) {
        if (!stack.getSender().hasPermission("lachshield.admin") || !stack.getSender().hasPermission("lachshield.commands")) {
            stack.getSender().sendMessage(configManager.getMessage("messages.noPermission"));
            return;
        }

        if (args.length == 0) {
            TextComponent message = Component.text()
                    .append(Component.text("LachShield v" + plugin.getPluginMeta().getVersion() + " - sub-commands", NamedTextColor.GOLD))
                    .appendNewline()
                    .append(Component.text("- /lachshield reload <config|all|feature> - reloads a specific component or everything"))
                    .appendNewline()
                    .append(Component.text("- /lachshield enable <feature> - reloads a specific feature"))
                    .appendNewline()
                    .append(Component.text("- /lachshield disable <feature> - disables a specific feature"))
                    .appendNewline()
                    .append(Component.text("- /lachshield iplimit <number> - set the player ip limit"))
                    .color(NamedTextColor.DARK_GREEN)
                    .build();

            stack.getSender().sendMessage(message);
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            if (args[1].equalsIgnoreCase("config")) {
                plugin.getConfigManager().reloadConfig();
                stack.getSender().sendMessage(configManager.getMessage("messages.reloadSuccess"));
                return;
            }

            if (args[1].equalsIgnoreCase("all")) {
                plugin.getConfigManager().reloadConfig();
                featureManager.getFeatureList().forEach(Feature::reload);
                stack.getSender().sendMessage(Component.text("All features reloaded", NamedTextColor.GREEN));
                return;
            }

            Feature feature = featureManager.getFeatureList().stream()
                    .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                    .findFirst()
                    .orElse(null);
            if (feature == null) {
                stack.getSender().sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return;
            }

            feature.reload();
            stack.getSender().sendMessage(Component.text("Feature " + feature.getFeatureName() + " reloaded", NamedTextColor.GREEN));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("enable")) {
            Feature feature = featureManager.getFeatureList().stream()
                    .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                    .findFirst()
                    .orElse(null);


            if (feature == null) {
                stack.getSender().sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return;
            }

            if (featureManager.enable(feature)) {
                stack.getSender().sendMessage(Component.text("Feature enabled " + feature.getFeatureName(), NamedTextColor.GREEN));
            } else {
                stack.getSender().sendMessage(Component.text("Feature is already enabled", NamedTextColor.RED));
            }
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("disable")) {
            Feature feature = featureManager.getFeatureList().stream()
                    .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                    .findFirst()
                    .orElse(null);
            if (feature == null) {
                stack.getSender().sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return;
            }

            if (featureManager.disable(feature)) {
                stack.getSender().sendMessage(Component.text("Feature disabled " + feature.getFeatureName(), NamedTextColor.GREEN));
            } else {
                stack.getSender().sendMessage(Component.text("Feature is already disabled", NamedTextColor.RED));
            }

            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("iplimit")) {
            try {
                int newLimit = Integer.parseInt(args[1]);
                if (newLimit < 1) {
                    stack.getSender().sendMessage(Component.text("The ip limit should be 1 or more", NamedTextColor.RED));
                    return;
                }
                plugin.getConfigManager().setMaxAccountsPerIP(newLimit);
                stack.getSender().sendMessage(Component.text("IP limit has been set to " + newLimit, NamedTextColor.GREEN));
            } catch (NumberFormatException e) {
                stack.getSender().sendMessage(Component.text("Invalid number format. Usage: /lachshield iplimit <number>", NamedTextColor.RED));
            }
        }

        if (args[0].equalsIgnoreCase("status")) {
            TextComponent.Builder statusBuilder = Component.text()
                    .append(Component.text("Enabled modules (" + featureManager.getEnabledFeatures().size() + "):", NamedTextColor.GOLD)).appendNewline();

            featureManager.getEnabledFeatures().forEach(feature -> statusBuilder.append(Component.text(feature.getFeatureName(), NamedTextColor.GREEN)).appendNewline());

            statusBuilder.append(Component.text("Disabled modules (" + featureManager.getDisabledFeatures().size() + "):", NamedTextColor.GOLD)).appendNewline();

            for (int i = 0; i < featureManager.getDisabledFeatures().size(); i++) {
                Feature currentFeature = featureManager.getDisabledFeatures().get(i);
                statusBuilder.append(Component.text(currentFeature.getFeatureName(), NamedTextColor.RED));
                if (i + 1 != featureManager.getDisabledFeatures().size()) {
                    statusBuilder.appendNewline();
                }
            }

            stack.getSender().sendMessage(statusBuilder);
        }
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandSourceStack stack, String @NotNull [] args) {
        if (!stack.getSender().hasPermission("lachshield.admin") || !stack.getSender().hasPermission("lachshield.commands")) {
            return List.of();
        }

        if (args.length <= 1) {
            return List.of("reload", "enable", "disable", "iplimit", "status");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            return Stream.concat(Stream.of("config", "all"), featureManager.getEnabledFeatures().stream().map(Feature::getFeatureName))
                    .filter(feature -> feature.startsWith(args[1]))
                    .toList();
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("enable"))) {
            return featureManager.getDisabledFeatures().stream()
                    .map(Feature::getFeatureName)
                    .filter(feature -> feature.startsWith(args[1]))
                    .toList();
        }

        if (args.length == 2 && (args[0].equalsIgnoreCase("disable"))) {
            return featureManager.getEnabledFeatures().stream()
                    .map(Feature::getFeatureName)
                    .filter(feature -> feature.startsWith(args[1]))
                    .toList();
        }

        return List.of();
    }
}
