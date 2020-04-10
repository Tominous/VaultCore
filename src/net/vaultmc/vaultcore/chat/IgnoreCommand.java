package net.vaultmc.vaultcore.chat;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.configuration.SQLPlayerData;
import net.vaultmc.vaultloader.utils.player.VLOfflinePlayer;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@RootCommand(literal = "ignore", description = "Stop seeing messages from a player.")
@Permission(Permissions.IgnoreCommand)
@PlayerOnly
public class IgnoreCommand extends CommandExecutor {

    public IgnoreCommand() {
        register("ignoreList", Collections.singletonList(Arguments.createLiteral("list")));
        register("ignore", Collections.singletonList(Arguments.createArgument("target", Arguments.offlinePlayerArgument())));
    }

    static Set<UUID> ignored;

    // TODO: Use seperate ignores table in db... then cache every 5 mins

    public static boolean isIgnoring(VLPlayer ignorer, VLPlayer ignoredPlayer) {
        /* ignorer is the player recieving the message/event
           ignoredPlayer is the executor of the event */
        SQLPlayerData data = ignorer.getPlayerData();
        String csvIgnored = data.getString("ignored");
        if (csvIgnored != null) {
            if (csvIgnored.isEmpty()) return false;
            ignored = Arrays.asList(csvIgnored.split(", ")).stream().map(UUID::fromString).collect(java.util.stream.Collectors.toSet());
            if (ignored.contains(ignoredPlayer.getUniqueId())) return true;
        }
        return false;
    }

    @SubCommand("ignore")
    public void ignore(VLPlayer sender, VLOfflinePlayer target) {
        if (target.getFirstPlayed() == 0L) {
            sender.sendMessage(VaultLoader.getMessage("vaultcore.player_never_joined"));
            return;
        }
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        if (csvIgnored != null) {
            Bukkit.getLogger().info(csvIgnored);
            ignored = Arrays.asList(csvIgnored.split(", ")).stream().map(UUID::fromString).collect(java.util.stream.Collectors.toSet());
            if (ignored.contains(target.getUniqueId())) {
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.already_ignored"), target.getFormattedName()));
            } else {
                data.set("ignored", csvIgnored + (ignored.size() < 1 ? "" : ", ") + target.getUniqueId());
                sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.toggle_ignored"), ChatColor.GREEN + "started", target.getFormattedName()));
            }
        } else {
            data.set("ignored", target.getUniqueId());
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.toggle_ignored"), ChatColor.GREEN + "started", target.getFormattedName()));
        }
    }

    @SubCommand("ignoreList")
    public void ignoreList(VLPlayer sender) {
        SQLPlayerData data = sender.getPlayerData();
        String csvIgnored = data.getString("ignored");
        sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.header"));
        if (csvIgnored != null) {
            Bukkit.getLogger().info(csvIgnored);
            if (csvIgnored.isEmpty()) {
                sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring_anyone"));
                return;
            }
            ignored = Arrays.asList(csvIgnored.split(", ")).stream().map(UUID::fromString).collect(java.util.stream.Collectors.toSet());
            if (ignored.size() > 0) {
                int count = 1;
                for (UUID uuid : ignored) {
                    // This sends exactly what it should
                    sender.sendMessage(uuid + ""); // Debugging purposes
                    // uuid SHOULD be a player uuid.
                    // However, on next line we get an Invalid UUID String error. I'm sure its a stupid mistake somewhere...
                    sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.ignore.list"), count, VLOfflinePlayer.getOfflinePlayer(uuid).getFormattedName()));
                    count++;
                }
            } else sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring_anyone"));
        } else sender.sendMessage(VaultLoader.getMessage("vaultcore.commands.ignore.not_ignoring_anyone"));
    }
}
