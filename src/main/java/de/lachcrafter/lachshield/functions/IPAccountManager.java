package de.lachcrafter.lachshield.functions;

import de.lachcrafter.lachshield.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class IPAccountManager {
    private final Map<String, Integer> ipAccountCount = new HashMap<>();
    private final ConfigManager configManager;

    public IPAccountManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public boolean handlePlayerJoin(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);
        int maxAccountsPerIP = configManager.getMaxAccountsPerIP();

        if (accountCount >= maxAccountsPerIP) {
            Component kickComponent = configManager.getKickMessage();
            player.kick(kickComponent);
            return false;
        }

        ipAccountCount.put(ip, accountCount + 1);
        return true;
    }

    public void handlePlayerQuit(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        int accountCount = ipAccountCount.getOrDefault(ip, 0);

        if (accountCount > 0) {
            ipAccountCount.put(ip, accountCount - 1);
        }
    }
}