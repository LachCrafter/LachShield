package de.lachcrafter.lachshield.lib;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import org.bukkit.event.Listener;

public abstract class NewFeature extends PacketListenerAbstract implements Listener {

    private final String name;
    private final boolean foliaCompatible;
    private boolean packetListener = false;

    public NewFeature(String name, boolean foliaCompatible, boolean packetListener) {
        this.name = name;
        this.foliaCompatible = foliaCompatible;
    }

    public NewFeature(String name, PacketListenerPriority priority, boolean packetListener,  boolean foliaCompatible) {
        super(priority);
        this.name = name;
        this.foliaCompatible = foliaCompatible;
        this.packetListener = packetListener;
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
     * @return If the feature listens to packets.
     */
    public boolean isPacketListener() {
        return packetListener;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onReload();
}
