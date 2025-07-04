package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import de.lachcrafter.lachshield.managers.ConfigManager;
import de.lachcrafter.lachshield.lib.PacketEventsFeature;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HidePlayerData extends PacketEventsFeature {
    private final ConfigManager configManager;
    private boolean stackSize;
    private boolean durability;
    private boolean health;
    private boolean onGround;
    private boolean difficulty;

    public HidePlayerData(ConfigManager configManager) {
        super(PacketListenerPriority.HIGHEST);
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
                                    .level(69)
                                    .build()
                    )));
                }
            });
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata em = new WrapperPlayServerEntityMetadata(event);

            List<EntityData<?>> datas = new ArrayList<>();
            for (EntityData<?> ed : em.getEntityMetadata()) {
                if (ed.getIndex() == 9 && health) {
                    float healthValue = ThreadLocalRandom.current().nextFloat() * (20f - 2f) + 2f;
                    if ((float) ed.getValue() == 0f) {
                        healthValue = 0;
                    }
                    datas.add(new EntityData<>(9, EntityDataTypes.FLOAT, healthValue));
                } else {
                    datas.add(ed);
                }
            }
            em.setEntityMetadata(datas);
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE && onGround) {
            WrapperPlayServerEntityRelativeMove erm = new WrapperPlayServerEntityRelativeMove(event);
            erm.setOnGround(false);
        }

        if (event.getPacketType() == PacketType.Play.Server.SERVER_DIFFICULTY && difficulty) {
            WrapperPlayServerDifficulty sd = new WrapperPlayServerDifficulty(event);
            sd.setDifficulty(Difficulty.PEACEFUL);
            sd.setLocked(true);
        }
    }

    @Override
    public String getFeatureName() {
        return "HidePlayerData";
    }

    @Override
    public void enable() {
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    @Override
    public void disable() {
        PacketEvents.getAPI().getEventManager().unregisterListener(this);
    }

    @Override
    public void reload() {
        FileConfiguration config = configManager.getConfig();
        this.stackSize = config.getBoolean("HidePlayerData.toObfuscate.stackSize", true);
        this.durability = config.getBoolean("HidePlayerData.toObfuscate.durability", true);
        this.health = config.getBoolean("HidePlayerData.toObfuscate.health", true);
        this.onGround = config.getBoolean("HidePlayerData.toObfuscate.onGround", true);
        this.difficulty = config.getBoolean("HidePlayerData.toObfuscate.difficulty", true);
    }
}