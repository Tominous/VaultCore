package net.vaultmc.vaultcore.commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;

public class DiscordCommand implements CommandExecutor {

	String string = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("string"));
	String variable1 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-1"));
	String variable2 = ChatColor.translateAlternateColorCodes('&',
			VaultCore.getInstance().getConfig().getString("variable-2"));

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("discord")) {

			if (!(sender instanceof Player)) {
				sender.sendMessage(Utilities.consoleError());
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission(Permissions.DiscordCommand)) {
				sender.sendMessage(Utilities.noPermission());
				return true;
			}

			try {

				String token = TokenCommand.getToken(player.getUniqueId(), player);

				player.sendMessage(string + "Your token: " + variable2 + token);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			player.sendMessage(string + "Click here to join our guild: " + variable1 + "https://discord.vaultmc.net");
			return true;
		}
		return true;
	}
}
