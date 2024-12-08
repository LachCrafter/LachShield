package de.lachcrafter.lachshield.lib;

import org.bukkit.event.Listener;

public interface Feature extends Listener {
    /**
     * @return the name of the feature used in the config
     */
    String getFeatureName();

    void enable();

    void disable();

    void reload();
}