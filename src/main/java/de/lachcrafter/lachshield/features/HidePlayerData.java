package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;

import static de.lachcrafter.lachshield.LachShield.configManager;

public class HidePlayerData extends Feature {
    PacketListener packetListener = new PacketListener();

    public HidePlayerData() {
        super("HidePlayerData", true);
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(packetListener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().getEventManager().unregisterListener(packetListener);
    }

    @Override
    public void onReload() {
        packetListener.reload();
    }
}

class PacketListener extends PacketListenerAbstract {

    public boolean stackSize;
    public boolean durability;
    public boolean health;
    public boolean enchantments;

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            WrapperPlayServerEntityEquipment ee = new WrapperPlayServerEntityEquipment(event);

            ee.getEquipment().forEach(eq -> {
                if (stackSize) {
                    eq.getItem().setAmount(1);
                }
                if (durability) {
                    eq.getItem().setDamageValue(0);
                }
                if (enchantments && eq.getItem().isEnchanted()) {
                    eq.getItem().setEnchantments(new ArrayList<>(Collections.singletonList(
                            new Enchantment.Builder()
                                    .type(EnchantmentTypes.ALL_DAMAGE_PROTECTION)
                                    .level(1)
                                    .build()
                    )));
                }
            });
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata em = new WrapperPlayServerEntityMetadata(event);

            if (health) {
                em.getEntityMetadata().removeIf(data -> data.getIndex() == 9);
            }
        }
    }

    public void reload() {
        FileConfiguration config = configManager.getConfig();
        this.stackSize = config.getBoolean("HidePlayerData.data.stackSize", true);
        this.durability = config.getBoolean("HidePlayerData.data.durability", true);
        this.health = config.getBoolean("HidePlayerData.data.health", true);
        this.enchantments = config.getBoolean("HidePlayerData.enchantments", true);
    }

}