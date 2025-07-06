package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import de.lachcrafter.lachshield.lib.NewFeature;
import de.lachcrafter.lachshield.managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HidePlayerData extends NewFeature {
    private final ConfigManager configManager;
    private boolean stackSize;
    private boolean durability;
    private boolean health;

    public HidePlayerData(ConfigManager configManager) {
        super("HidePlayerData", PacketListenerPriority.HIGHEST);
        this.configManager = configManager;
    }

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
                if (eq.getItem().isEnchanted()) {
                    eq.getItem().setEnchantments(new ArrayList<>(Collections.singletonList(
                            new Enchantment.Builder()
                                    .type(EnchantmentTypes.ALL_DAMAGE_PROTECTION)
                                    .level(new Random().nextInt(1, 60))
                                    .build()
                    )));
                }
            });
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata em = new WrapperPlayServerEntityMetadata(event);

            List<EntityData<?>> datas = new ArrayList<>();
            if (health) {
                em.getEntityMetadata().removeIf(data -> data.getIndex() == 9);
            }
            em.setEntityMetadata(datas);
        }
    }

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().getEventManager().unregisterListener(this);
    }

    @Override
    public void onReload() {
        FileConfiguration config = configManager.getConfig();
        this.stackSize = config.getBoolean("HidePlayerData.data.stackSize", true);
        this.durability = config.getBoolean("HidePlayerData.data.durability", true);
        this.health = config.getBoolean("HidePlayerData.data.health", true);
    }
}