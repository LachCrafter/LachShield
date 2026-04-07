package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import de.lachcrafter.lachshield.LachShield;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class HidePlayerData extends Feature {
    private final EventManager eventManager = PacketEvents.getAPI().getEventManager();
    private final PlayerDataListener playerDataListener;
    protected static boolean hideHealth, hideEnchantments, hideDurability, hideStackSize, hideGameMode;

    public HidePlayerData() {
        super("HidePlayerData", true);
        playerDataListener = new PlayerDataListener();
    }

    @Override
    public void onEnable() {
        eventManager.registerListener(playerDataListener);
    }

    @Override
    public void onDisable() {
        eventManager.unregisterListener(playerDataListener);
    }

    @Override
    public void onReload() {
        var config = LachShield.configManager.getConfig();

        hideHealth = config.getBoolean(getName() + ".data.health");
        hideEnchantments = config.getBoolean(getName() + ".data.enchantments");
        hideDurability = config.getBoolean(getName() + ".data.durability");
        hideStackSize = config.getBoolean(getName() + ".data.stackSize");
        hideGameMode = config.getBoolean(getName() + ".data.gameMode");
    }
}

class PlayerDataListener extends PacketListenerAbstract {

    @Override
    public void onPacketSend(@NonNull PacketSendEvent event) {

        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO_UPDATE) {
            WrapperPlayServerPlayerInfoUpdate packet = new WrapperPlayServerPlayerInfoUpdate(event);

            if (HidePlayerData.hideGameMode) {
                packet.getEntries().forEach(playerInfo -> playerInfo.setGameMode(GameMode.ADVENTURE));
            }
        }

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
            WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(event);

            packet.getEquipment().forEach(equipment -> {
                var itemStack = equipment.getItem();

                if (HidePlayerData.hideStackSize) {
                    itemStack.setAmount(1);
                }

                if (HidePlayerData.hideEnchantments && itemStack.isEnchanted()) {
                    itemStack.setEnchantments(List.of(
                            Enchantment.builder()
                                .type(EnchantmentTypes.ALL_DAMAGE_PROTECTION)
                                .level(1)
                                .build()));
                }

                if (HidePlayerData.hideDurability) {
                    itemStack.setDamageValue(0);
                }
            });
        }

        if (HidePlayerData.hideHealth && event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event);

            packet.getEntityMetadata().removeIf(entityData -> entityData.getIndex() == 9);
        }
    }

}