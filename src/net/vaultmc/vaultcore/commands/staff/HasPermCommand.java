package net.vaultmc.vaultcore.commands.staff;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.ChatColor;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.Arguments;
import net.vaultmc.vaultloader.utils.commands.CommandExecutor;
import net.vaultmc.vaultloader.utils.commands.Permission;
import net.vaultmc.vaultloader.utils.commands.PlayerOnly;
import net.vaultmc.vaultloader.utils.commands.RootCommand;
import net.vaultmc.vaultloader.utils.commands.SubCommand;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

@RootCommand(literal = "hasperm", description = "Check if a player has a permission.")
@Permission(Permissions.HasPermCommand)
public class HasPermCommand extends CommandExecutor {

	public HasPermCommand() {
		this.register("hasPermSelf",
				Collections.singletonList(Arguments.createArgument("permission", Arguments.string())));
		this.register("hasPermOther", Arrays.asList(Arguments.createArgument("permission", Arguments.string()),
				Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
	}

	@SubCommand("hasPermSelf")
	@PlayerOnly
	public void hasPermSelf(VLPlayer sender, String permission) {
		String status = null;
		if (sender.hasPermission(permission)) {
			status = ChatColor.GREEN + "have";
		} else {
			status = ChatColor.RED + "don't have";
		}
		sender.sendMessage(
				Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.hasperm.self"), status, permission));
	}

	@SubCommand("hasPermOther")
	@Permission(Permissions.HasPermCommandOther)
	public void hasPermOther(VLCommandSender sender, String permission, VLOfflinePlayer target) {
		if (target.getFirstPlayed() == 0L) {
			sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
			return;
		}
		String status = null;
		if (target.hasPermission(permission)) {
			status = ChatColor.GREEN + "has";
		} else {
			status = ChatColor.RED + "doesn't have";
		}
		sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.hasperm.other"),
				target.getFormattedName(), status, permission));
	}
}
