package de.lachcrafter.lachshield.managers;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.features.AntiAfk;
import de.lachcrafter.lachshield.features.AntiNetherRoof;
import de.lachcrafter.lachshield.features.AntiPearlPhase;
import de.lachcrafter.lachshield.features.Feature;
import de.lachcrafter.lachshield.features.HidePlayerData;
import de.lachcrafter.lachshield.features.ChatFilter;
import de.lachcrafter.lachshield.features.IPAccountManager;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {
    private final LachShield plugin;
    private final ConfigManager configManager;

    private final List<Feature> allFeatures, registeredFeatures, enabledFeatures, incompatibleFeatures;

    public FeatureManager(LachShield plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        this.allFeatures = new ArrayList<>();
        this.registeredFeatures = new ArrayList<>();
        this.enabledFeatures = new ArrayList<>();
        this.incompatibleFeatures = new ArrayList<>();

        // List all features here.
        List<Feature> features = new ArrayList<>(List.of(

                new AntiAfk(plugin, configManager),
                new AntiNetherRoof(plugin, configManager),
                new AntiPearlPhase(plugin),
                new ChatFilter(plugin),
                new IPAccountManager(plugin, configManager)

        ));

        // List PacketEvents-dependent features here.
        if (plugin.getServer().getPluginManager().isPluginEnabled("packetevents")) {
            features.add(new HidePlayerData());
        } else {
            LachShield.LOGGER.warn("PacketEvents has not been found and features that depend on PacketEvents will not be loaded.");
        }

        // Register the features listed.
        initFeatures(features);

        // Load all features.
        loadFeatures();
    }

    /**
     * Registers a feature.
     * @param feature the feature to register.
     */
    public void registerFeature(Feature feature) {
        if (!registeredFeatures.contains(feature) && !incompatibleFeatures.contains(feature)) {
            registeredFeatures.add(feature);
            LachShield.LOGGER.info("Feature enabled: {}", feature.getName());
        }
    }

    /**
     * Initializes all features defined.
     * This should only be called once.
     */
    public void initFeatures(List<Feature> features) {
        features.forEach(feature -> {

            if (LachShield.isFolia() && !feature.isFoliaCompatible()) {
                incompatibleFeatures.add(feature);
                LachShield.LOGGER.warn("Feature {} not enabled due to Folia limitations.", feature.getName());
            }

            registerFeature(feature);
        });
    }

    /**
     * Enables all features that have been enabled in the config.
     */
    public void loadFeatures() {
        registeredFeatures.forEach(feature -> {

            if (configManager.isFeatureEnabled(feature.getName())) {
                enableFeature(feature);
            }

        });
    }

    /**
     * Get all enabled features as a List
     * @return all enabled features in a list.
     */
    public List<Feature> getEnabledFeatures() {
        return enabledFeatures;
    }

    /**
     * Get all disabled features as a List
     * @return all disabled features in a list.
     */
    public List<Feature> getDisabledFeatures() {
        return allFeatures.stream()
                .filter(newFeature -> !enabledFeatures.contains(newFeature))
                .toList();
    }

    /**
     * Get all available registered features.
     * @return all available features.
     */
    public List<Feature> getRegisteredFeatures() {
        return registeredFeatures;
    }

    /**
     * Enables a feature.
     * @param feature the feature to enable.
     * @return Returns true when the feature has been enabled, or false when it is already enabled.
     */
    public boolean enableFeature(Feature feature) {
        if (!enabledFeatures.contains(feature)) {
            enabledFeatures.add(feature);
            configManager.setFeatureEnabled(feature.getName(), true);
            feature.onEnable();
            feature.onReload();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Disables a feature.
     * @param feature the feature to disable.
     * @return Returns true when the feature has been disabled, or false when it is already disabled.
     */
    public boolean disableFeature(Feature feature) {
        if (enabledFeatures.contains(feature)) {
            feature.onDisable();
            enabledFeatures.remove(feature);
            configManager.setFeatureEnabled(feature.getName(), false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reloads a feature.
     * @param feature the feature to reload.
     * @return Returns true if the feature has been reloaded, or false when the feature is disabled.
     */
    public boolean reloadFeature(Feature feature) {
        if (!enabledFeatures.contains(feature)) {
            feature.onReload();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get a feature by its name.
     * @param name name of the feature.
     */
    public Feature getFeatureByName(String name) {
        return registeredFeatures.stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
