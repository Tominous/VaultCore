package net.vaultmc.vaultcore.teleport.tpa;

import lombok.Getter;
import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.chat.IgnoreCommand;
import net.vaultmc.vaultcore.settings.PlayerSettings;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collections;
import java.util.HashMap;

@RootCommand(literal = "tpa", description = "Request to teleport to a player.")
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPACommand extends CommandExecutor implements Listener {

    @Getter
    public HashMap<VLPlayer, VLPlayer> tpaRequests = new HashMap<>();

    public TPACommand() {
        register("tpa", Collections.singletonList(Arguments.createArgument("target", Arguments.playerArgument())));
    }

    @SubCommand("tpa")
    public void tpa(VLPlayer sender, VLPlayer target) {
        if (verifyRequest(sender, target)) return;
        if (PlayerSettings.getSetting(target, "settings.autotpa")) {
            sender.teleport(target.getLocation());
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.auto_accept_sender"), target.getFormattedName()));
            target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.auto_accept_target"), sender.getFormattedName()));
            return;
        }
        if (tpaRequests.containsKey(target)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.pending_error"));
            return;
        }
        if (tpaRequests.containsValue(sender)) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_sender"), tpaRequests.get(sender).getFormattedName()));
            tpaRequests.get(sender).sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.overrode_request_target"), sender.getFormattedName()));
            // No need to remove, as it will be overwritten automatically
        }
        tpaRequests.put(target, sender);
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.request_sent"), target.getFormattedName()));
        target.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.tpa.request_received"), sender.getFormattedName()));
    }

    public static boolean verifyRequest(VLPlayer sender, VLPlayer target) {
        if (sender == target) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.teleport.self_error"));
            return true;
        }
        if (IgnoreCommand.isIgnoring(target, sender)) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.you_are_ignored"));
            return true;
        }
        if (!PlayerSettings.getSetting(target, "settings.tpa")) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.tpa.requests.disabled_tpa"));
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        tpaRequests.remove(VLPlayer.getPlayer(e.getPlayer()));
    }
}