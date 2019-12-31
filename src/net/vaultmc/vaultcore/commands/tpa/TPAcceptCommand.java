package net.vaultmc.vaultcore.commands.tpa;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

@RootCommand(
        literal = "tpaccept",
        description = "Request to teleport to a player."
)
@Permission(Permissions.TPACommand)
@PlayerOnly
public class TPAcceptCommand extends CommandExecutor {
    private static HashMap<UUID, UUID> requests = TPACommand.getRequests();
    private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

    public TPAcceptCommand() {
        register("tpaccept", Collections.emptyList());
    }

    @SubCommand("tpaccept")
    public void tpaccept(CommandSender sender) {
        String string = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("string"));
        String variable1 = ChatColor.translateAlternateColorCodes('&',
                VaultCore.getInstance().getConfig().getString("variable-1"));

        Player player = (Player) sender;
        if (requests.containsKey(player.getUniqueId())) {
            player.sendMessage(string + "You have accepted the teleport request.");
            Bukkit.getPlayer(requests.get(player.getUniqueId()))
                    .sendMessage(variable1 + VaultCoreAPI.getName(player) + string + " accepted your teleport request.");
            Bukkit.getPlayer(requests.get(player.getUniqueId())).teleport(player);
            requests.remove(player.getUniqueId());
            return;
        }
        if (requestsHere.containsKey(player.getUniqueId())) {

            Player target = Bukkit.getPlayer(requestsHere.get(player.getUniqueId()));

            player.sendMessage(string + "You have accepted the teleport request.");
            target.sendMessage(variable1 + VaultCoreAPI.getName(player) + string + " accepted your teleport request.");
            player.teleport(target);
            requestsHere.remove(player.getUniqueId());
        } else {
            player.sendMessage(ChatColor.RED + "You don't have a pending request!");
        }
    }
}