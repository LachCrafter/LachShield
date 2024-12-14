package de.lachcrafter.lachshield.lib;

import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.LachShield;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {
    private final ConfigManager configManager;
    private final List<Feature> featureList;
    private final List<Feature> enabledFeatures;

    public FeatureManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.featureList = new ArrayList<>();
        this.enabledFeatures = new ArrayList<>();
    }

    public void register(Feature feature) {
        featureList.add(feature);
    }

    public void unregister(Feature feature) {
        featureList.remove(feature);
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public List<Feature> getEnabledFeatures() {
        return enabledFeatures;
    }

    public List<Feature> getDisabledFeatures() {
        return featureList.stream()
            .filter(feature -> !enabledFeatures.contains(feature))
            .toList();
    }

    public void load() {
        for (Feature feature : featureList) {
            if (configManager.isFeatureEnabled(feature.getFeatureName())) {
                enable(feature);
            }
        }
    }

    public void enable(Feature feature) {
        if (!enabledFeatures.contains(feature)) {
            enabledFeatures.add(feature);
            feature.reload();
            feature.enable();
            configManager.setFeatureEnabled(feature, true);
            LachShield.LOGGER.info("Enabled feature: {}", feature.getFeatureName());
        }
    }

    public void disable(Feature feature) {
        if (enabledFeatures.contains(feature)) {
            enabledFeatures.remove(feature);
            feature.disable();
            configManager.setFeatureEnabled(feature, false);
            LachShield.LOGGER.info("Disabled feature: {}", feature.getFeatureName());
        }
    }
}