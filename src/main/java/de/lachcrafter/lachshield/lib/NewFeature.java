package de.lachcrafter.lachshield.lib;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

public abstract class NewFeature extends PacketListenerAbstract {

    private final String name;

    public NewFeature(String name) {
        this.name = name;
    }

    public NewFeature(String name, PacketListenerPriority priority) {
        super(priority);
        this.name = name;
    }

    /**
     * @return the feature name
     */
    public String getName() {
        return name;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();
}
