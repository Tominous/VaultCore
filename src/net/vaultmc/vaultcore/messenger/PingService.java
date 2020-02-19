package net.vaultmc.vaultcore.messenger;

import lombok.Getter;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import net.vaultmc.vaultloader.utils.messenger.MessageReceivedEvent;
import net.vaultmc.vaultloader.utils.messenger.SQLMessenger;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class PingService extends ConstructorRegisterListener {
    @Getter
    private static final Map<String, Integer> pong = new HashMap<>();

    @EventHandler
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getMessage().startsWith("Ping")) {
            SQLMessenger.sendGlobalMessage("Pong" + VaultCore.SEPARATOR + e.getMessage().split(VaultCore.SEPARATOR)[1] +
                    VaultCore.SEPARATOR + "VaultCore at your service.");
        } else if (e.getMessage().startsWith("Pong")) {
            int i = pong.getOrDefault(e.getMessage().split(VaultCore.SEPARATOR)[1], 0);
            i++;
            pong.put(e.getMessage().split(VaultCore.SEPARATOR)[1], i);
        }
    }

    public static void ping(String session) {
        SQLMessenger.sendGlobalMessage("Ping" + VaultCore.SEPARATOR + session);
    }
}