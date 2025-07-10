package de.lachcrafter.lachshield.features;

import org.bukkit.entity.Player;
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

    /**
     * @return the permission for this feature.
     */
    public String getPermission() {
        return "lachshield." + name.toLowerCase();
    }

    /**
     * Checks if the player has the bypass/use permission.
     * @param player player object to check
     * @return if the player has the permission
     */
    public boolean hasFeaturePermission(Player player) {
        return player.hasPermission(getPermission()) && player.hasPermission("lachshield.admin");
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();
}
