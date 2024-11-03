package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerObfuscator extends PacketListenerAbstract {

    private boolean enabled, stackSize, durability, enchantments, health, onGround, difficulty;

    public PlayerObfuscator() {
        super(PacketListenerPriority.HIGHEST);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (!enabled) return;

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            WrapperPlayServerEntityEquipment ee = new WrapperPlayServerEntityEquipment(event);

            ee.getEquipment().forEach(eq -> {
                if (stackSize) {
                    eq.getItem().setAmount(1);
                }
                if (durability) {
                    eq.getItem().setDamageValue(0);
                }
                if (eq.getItem().isEnchanted(event.getUser().getClientVersion()) && enchantments) {
                    eq.getItem().setEnchantments(new ArrayList<>(Collections.singletonList(
                            new Enchantment.Builder()
                                    .type(EnchantmentTypes.ALL_DAMAGE_PROTECTION)
                                    .level(69)
                                    .build()
                    )), event.getUser().getClientVersion());
                }
            });
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata em = new WrapperPlayServerEntityMetadata(event);

            ArrayList<EntityData> datas = new ArrayList<>();
            for (EntityData ed : em.getEntityMetadata()) {
                if (ed.getIndex() == 9 && health) {
                    float healthValue = ThreadLocalRandom.current().nextFloat() * (20f - 2f) + 2f;
                    if ((float) ed.getValue() == 0f) {
                        healthValue = 0;
                    }
                    datas.add(new EntityData(9, EntityDataTypes.FLOAT, healthValue));
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

    public void reload(FileConfiguration config) {
        this.enabled = config.getBoolean("obfuscate-player-data.enabled", false);
        this.stackSize = config.getBoolean("obfuscate-player-data.toObfuscate.stackSize", true);
        this.durability = config.getBoolean("obfuscate-player-data.toObfuscate.durability", true);
        this.enchantments = config.getBoolean("obfuscate-player-data.toObfuscate.enchantments", true);
        this.health = config.getBoolean("obfuscate-player-data.toObfuscate.health", true);
        this.onGround = config.getBoolean("obfuscate-player-data.toObfuscate.onGround", true);
        this.difficulty = config.getBoolean("obfuscate-player-data.toObfuscate.difficulty", true);
    }
}
