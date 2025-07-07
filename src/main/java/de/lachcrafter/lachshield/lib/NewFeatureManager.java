package de.lachcrafter.lachshield.lib;

import de.lachcrafter.lachshield.LachShield;
import de.lachcrafter.lachshield.features.*;
import de.lachcrafter.lachshield.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class NewFeatureManager {
    private final LachShield plugin;
    private final ConfigManager configManager;

    private final List<NewFeature> allFeatures, registeredFeatures, enabledFeatures, incompatibleFeatures;

    public NewFeatureManager(LachShield plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();

        this.allFeatures = new ArrayList<>();
        this.registeredFeatures = new ArrayList<>();
        this.enabledFeatures = new ArrayList<>();
        this.incompatibleFeatures = new ArrayList<>();

        // List all features here.
        initFeatures(List.of(

                new AntiAfk(plugin, configManager),
                new AntiNetherRoof(plugin, configManager),
                new AntiPearlPhase(plugin),
                new ChatFilter(plugin),
                new HidePlayerData(configManager),
                new IPAccountManager(plugin, configManager)

        ));

        // Load all features.
        loadFeatures();
    }

    /**
     * Registers a feature.
     * @param feature the feature to register.
     */
    public void registerFeature(NewFeature feature) {
        if (!registeredFeatures.contains(feature) && !incompatibleFeatures.contains(feature)) {
            registeredFeatures.add(feature);
        }
    }


    /**
     * Adds a feature to a {@link List} of features that can't be used at the moment due to incompatibility or missing dependencies.
     * @param feature the feature to add.
     */
    public void addIncompatibleFeature(NewFeature feature) {
        if (!incompatibleFeatures.contains(feature)) {
            incompatibleFeatures.add(feature);
        }
    }

    /**
     * Initializes all features defined.
     * It also checks for dependencies and server software for each feature.
     * This should only be called once.
     */
    public void initFeatures(List<NewFeature> features) {
        features.forEach(
                feature -> {

                    // Add all features to the allFeatures list.
                    allFeatures.add(feature);

                    // Check for Folia compatibility.
                    if (LachShield.isFolia() && !feature.isFoliaCompatible()) {
                        addIncompatibleFeature(feature);
                    }

                    // Check for PacketEvents dependency.
                    if (!plugin.getServer().getPluginManager().isPluginEnabled("packetevents") && feature.isPacketListener()) {
                        addIncompatibleFeature(feature);
                    }

                    registerFeature(feature);
                }
        );
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
     * Get all features as a List
     * @return all features in a list.
     */
    public List<NewFeature> getAllFeatures() {
        return allFeatures;
    }

    /**
     * Get all enabled features as a List
     * @return all enabled features in a list.
     */
    public List<NewFeature> getEnabledFeatures() {
        return enabledFeatures;
    }

    /**
     * Get all disabled features as a List
     * @return all disabled features in a list.
     */
    public List<NewFeature> getDisabledFeatures() {
        return allFeatures.stream()
                .filter(newFeature -> !enabledFeatures.contains(newFeature))
                .toList();
    }

    /**
     * Enables a feature.
     * @param feature the feature to enable.
     * @return Returns true when the feature has been enabled, or false when it is already enabled.
     */
    public boolean enableFeature(NewFeature feature) {
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
    public boolean disableFeature(NewFeature feature) {
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
    public boolean reloadFeature(NewFeature feature) {
        if (!enabledFeatures.contains(feature)) {
            feature.onReload();
            return true;
        } else {
            return false;
        }
    }

}
