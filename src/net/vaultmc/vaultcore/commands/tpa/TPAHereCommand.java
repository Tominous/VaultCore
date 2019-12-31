package net.vaultmc.vaultcore.commands.tpa;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultcore.VaultCoreAPI;
import net.vaultmc.vaultutils.utils.commands.experimental.Arguments;
import net.vaultmc.vaultutils.utils.commands.experimental.CommandExecutor;
import net.vaultmc.vaultutils.utils.commands.experimental.Permission;
import net.vaultmc.vaultutils.utils.commands.experimental.PlayerOnly;
import net.vaultmc.vaultutils.utils.commands.experimental.RootCommand;
import net.vaultmc.vaultutils.utils.commands.experimental.SubCommand;

@RootCommand(literal = "tpahere", description = "Request for a player to teleport you.")
@Permission(Permissions.TPAHereCommand)
@PlayerOnly
public class TPAHereCommand extends CommandExecutor {
	private static HashMap<UUID, UUID> requestsHere = TPACommand.getRequestsHere();

	public TPAHereCommand() {
		register("tpahere", Collections.singletonList(Arguments.createArgument("player", Arguments.playerArgument())));
	}

	@SubCommand("tpahere")
	public void tpaHere(CommandSender sender, Player target) {
		String string = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("string"));
		String variable1 = ChatColor.translateAlternateColorCodes('&',
				VaultCore.getInstance().getConfig().getString("variable-1"));

		Player player = (Player) sender;
		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't teleport to yourself!");
			return;
		}
		if (!VaultCore.getInstance().getPlayerData()
				.getBoolean("players." + target.getUniqueId() + ".settings.tpa")) {
			player.sendMessage(ChatColor.RED + "That player has disabled TPAs!");
			return;
		}
		requestsHere.put(target.getUniqueId(), player.getUniqueId());
		player.sendMessage(string + "You requested that " + variable1 + VaultCoreAPI.getName(target) + string
				+ " teleports to you.");
		target.sendMessage(variable1 + VaultCoreAPI.getName(player) + string + " asked you to teleport to them, type "
				+ variable1 + "/tpaccept " + string + "to accept it.");
	}
}