package de.lachcrafter.lachshield.lib;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.bukkit.event.Listener;

public abstract class NewFeature extends PacketListenerAbstract implements Listener {

    private final String name;
    private final boolean foliaCompatible;

    public NewFeature(String name, boolean foliaCompatible) {
        this.name = name;
        this.foliaCompatible = foliaCompatible;
    }

    public NewFeature(String name, PacketListenerPriority priority, boolean foliaCompatible) {
        super(priority);
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
