package de.lachcrafter.lachshield.features;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.EventManager;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import de.lachcrafter.lachshield.LachShield;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
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

        if (event.getPacketType() == PacketType.Play.Server.ENTITY_METADATA) {
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event);

            if (HidePlayerData.hideDurability) {
                var playerItemStack = packet.readItemStack();

                playerItemStack.setDamageValue(1);
                packet.writeItemStack(playerItemStack);
            }

            if (HidePlayerData.hideEnchantments) {
                var playerItemStack = packet.readItemStack();

                if (playerItemStack.isEnchanted()) {
                    playerItemStack.setEnchantments(List.of(Enchantment.builder()
                            .type(EnchantmentTypes.ALL_DAMAGE_PROTECTION)
                            .level(1)
                            .build()));

                    packet.writeItemStack(playerItemStack);
                }
            }

            if (HidePlayerData.hideStackSize) {
                var playerItemStack = packet.readItemStack();

                playerItemStack.setAmount(1);
                packet.writeItemStack(playerItemStack);
            }

            if (HidePlayerData.hideGameMode) {
                packet.writeGameMode(GameMode.ADVENTURE);
            }

            if (HidePlayerData.hideHealth) {
                List<EntityData<?>> entityDataList = new ArrayList<>();

                for (EntityData<?> data : packet.getEntityMetadata()) {

                    if (data.getIndex() == 9) break;
                    entityDataList.add(data);
                }
                packet.setEntityMetadata(entityDataList);
            }

            event.markForReEncode(true);
        }
    }

}