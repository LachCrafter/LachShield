package de.lachcrafter.lachshield.lib;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;

public class PacketEventsFeature extends PacketListenerAbstract implements Feature {
    public PacketEventsFeature(PacketListenerPriority priority) {
        super(priority);
    }

    @Override
    public String getFeatureName() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void enable() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void disable() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}