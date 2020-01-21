package net.vaultmc.vaultcore.commands;

import net.vaultmc.vaultcore.Permissions;
import net.vaultmc.vaultcore.Utilities;
import net.vaultmc.vaultcore.VaultCore;
import net.vaultmc.vaultloader.VaultLoader;
import net.vaultmc.vaultloader.utils.commands.*;
import net.vaultmc.vaultloader.utils.player.VLCommandSender;
import net.vaultmc.vaultloader.utils.player.VLPlayer;

import java.util.Arrays;
import java.util.Collections;

@RootCommand(literal = "warp", description = "Teleport to a warp.")
@Permission(Permissions.WarpCommand)
@PlayerOnly
public class WarpCommand extends CommandExecutor {

    public WarpCommand() {
        register("warp", Collections.singletonList(Arguments.createArgument("warp", Arguments.word())));
        register("setWarp",
                Arrays.asList(Arguments.createLiteral("set"), Arguments.createArgument("name", Arguments.word())));
        register("delWarp",
                Arrays.asList(Arguments.createLiteral("delete"), Arguments.createArgument("name", Arguments.word())));
    }

    @SubCommand("warp")
    public void warp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.not_exist"), warp));
        } else {
            sender.teleport(VaultCore.getInstance().getLocationFile().getLocation("warps." + warp));
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.teleported"), warp));
        }
    }

    @SubCommand("setWarp")
    @Permission(Permissions.WarpCommandSet)
    public void setWarp(VLPlayer sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            VaultCore.getInstance().getLocationFile().set("warps." + warp, sender.getLocation());
            VaultCore.getInstance().saveLocations();
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.set"), warp));
        } else {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.already_exist"), warp));
        }
    }

    @SubCommand("delWarp")
    @Permission(Permissions.WarpCommandDelete)
    public void delWarp(VLCommandSender sender, String warp) {
        if (VaultCore.getInstance().getLocationFile().get("warps." + warp) == null) {
            sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.not_exist"), warp));
            return;
        }

        VaultCore.getInstance().getLocationFile().set("warps." + warp, null);
        VaultCore.getInstance().saveLocations();
        sender.sendMessage(Utilities.formatMessage(VaultLoader.getMessage("vaultcore.commands.warp.deleted"), warp));
    }
}