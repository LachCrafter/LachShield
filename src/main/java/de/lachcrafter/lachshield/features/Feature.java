package de.lachcrafter.lachshield.features;

import org.bukkit.event.Listener;

public abstract class Feature implements Listener {

    private final String name;
    private final boolean foliaCompatible;

    public Feature(String name, boolean foliaCompatible) {
        this.name = name;
        this.foliaCompatible = foliaCompatible;
    }

    /**
     * @return the feature name
     */
    public String getName() {
        return name;
    }

    /**
     * @return If the feature works on Folia or not.
     */
    public boolean isFoliaCompatible() {
        return foliaCompatible;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();
}
