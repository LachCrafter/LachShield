package de.lachcrafter.lachshield.commands;

import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.lib.Feature;
import de.lachcrafter.lachshield.lib.FeatureManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import de.lachcrafter.lachshield.LachShield;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class LachShieldCommandOld implements TabExecutor {
    private final LachShield plugin;
    private final ConfigManager configManager;
    private final FeatureManager featureManager;

    public LachShieldCommandOld(LachShield plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.featureManager = plugin.getFeatureManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("lachshield.admin")) {
            sender.sendMessage(configManager.getNoPermission());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Component.text("LachShield v" + plugin.getPluginMeta().getVersion(), NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Usage: /lachshield reload <config|all|feature>", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Usage: /lachshield enable <feature>", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Usage: /lachshield disable <feature>", NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Usage: /lachshield iplimit <number>", NamedTextColor.GREEN));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            if (args[1].equalsIgnoreCase("config")) {
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage(Component.text("Config reloaded", NamedTextColor.GREEN));
                return true;
            }

            if (args[1].equalsIgnoreCase("all")) {
                plugin.getConfigManager().reloadConfig();
                featureManager.getFeatureList().forEach(Feature::reload);
                sender.sendMessage(Component.text("All features reloaded", NamedTextColor.GREEN));
                return true;
            }

            Feature feature = featureManager.getFeatureList().stream()
                .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);
            if (feature == null) {
                sender.sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return true;
            }

            feature.reload();
            sender.sendMessage(Component.text("Feature " + feature.getFeatureName() + " reloaded", NamedTextColor.GREEN));
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("enable")) {
            Feature feature = featureManager.getDisabledFeatures().stream()
                .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);
            if (feature == null) {
                sender.sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return true;
            }

            featureManager.enable(feature);
            sender.sendMessage(Component.text("Feature enabled " + feature.getFeatureName(), NamedTextColor.GREEN));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("disable")) {
            Feature feature = featureManager.getEnabledFeatures().stream()
                .filter(f -> f.getFeatureName().equalsIgnoreCase(args[1]))
                .findFirst()
                .orElse(null);
            if (feature == null) {
                sender.sendMessage(Component.text("Feature not found", NamedTextColor.RED));
                return true;
            }

            featureManager.disable(feature);
            sender.sendMessage(Component.text("Feature disabled " + feature.getFeatureName(), NamedTextColor.GREEN));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("iplimit")) {
            try {
                int newLimit = Integer.parseInt(args[1]);
                if (newLimit < 1) {
                    sender.sendMessage(Component.text("The ip limit should be 1 or more", NamedTextColor.RED));
                    return true;
                }
                plugin.getConfigManager().setMaxAccountsPerIP(newLimit);
                sender.sendMessage(Component.text("IP limit has been set to " + newLimit, NamedTextColor.GREEN));
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid number format. Usage: /lachshield iplimit <number>", NamedTextColor.RED));
            }
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload", "enable", "disable", "iplimit");
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