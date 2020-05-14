package net.vaultmc.vaultcore.pvp.utils;

import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.ConstructorRegisterListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GoldenAppleDelay extends ConstructorRegisterListener {
    private static final Set<UUID> gapplePlayers = new HashSet<>();
    private static final Set<UUID> napplePlayers = new HashSet<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (!e.getPlayer().getWorld().getName().equals("Pvp")) {
            return;
        }
        gapplePlayers.remove(e.getPlayer().getUniqueId());
        napplePlayers.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent e) {
        if (!e.getPlayer().getWorld().getName().equals("Pvp")) {
            return;
        }

        if (e.getItem().getType() == Material.GOLDEN_APPLE) {
            if (gapplePlayers.contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(ChatColor.RED + "You must wait at least 15 seconds before consuming another golden apple!");
                e.setCancelled(true);
            } else {
                gapplePlayers.add(e.getPlayer().getUniqueId());
                Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "You may now consume golden apples.");
                    gapplePlayers.remove(e.getPlayer().getUniqueId());
                }, 300L);
            }
        } else if (e.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            if (napplePlayers.contains(e.getPlayer().getUniqueId())) {
                e.getPlayer().sendMessage(ChatColor.RED + "You must wait at least 30 seconds before consuming another enchanted golden apple!");
                e.setCancelled(true);
            } else {
                napplePlayers.add(e.getPlayer().getUniqueId());
                Bukkit.getScheduler().runTaskLater(VaultLoader.getInstance(), () -> {
                    e.getPlayer().sendMessage(ChatColor.YELLOW + "You may now consume enchanted golden apples.");
                    napplePlayers.remove(e.getPlayer().getUniqueId());
                }, 600L);
            }
        }
    }
}
