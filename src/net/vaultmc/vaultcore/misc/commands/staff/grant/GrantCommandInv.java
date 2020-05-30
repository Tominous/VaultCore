/*
 * VaultCore contains the basic functionalities for VaultMC.
 * Copyright (C) 2020 VaultMC
 *
 * VaultCore is a proprietary software: you may not redistribute/use it
 * without prior permission from its owner, however you may contribute
 * to the code. by contributing to VaultCore, you grant to VaultMC a
 * perpetual, nonexclusive, transferable, royalty-free and worldwide
 * license to use, host, reproduce, modify, adapt, publish, translate,
 * create derivative works from, distribute, perform, and display your
 * contribution.
 */

package net.vaultmc.vaultcore.misc.commands.staff.grant;

import net.vaultmc.vaultloader.utils.ItemStackBuilder;
import net.vaultmc.vaultloader.utils.player.VLPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public final class GrantCommandInv {
    private GrantCommandInv() {
    }

    public static Inventory getGrantInventoryAdmin(VLPlayer target) {
        Inventory rankGrantAdmin = Bukkit.createInventory(null, 9,
                ChatColor.DARK_GRAY + "Grant Rank to " + target.getName());
        rankGrantAdmin.setItem(0, null);
        rankGrantAdmin.setItem(1, null);
        rankGrantAdmin.setItem(2, new ItemStackBuilder(Material.LIGHT_GRAY_WOOL)
                .name(ChatColor.GRAY + "" + ChatColor.BOLD + "Member").build());
        rankGrantAdmin.setItem(3, new ItemStackBuilder(Material.WHITE_WOOL)
                .name(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon").build());
        rankGrantAdmin.setItem(4, new ItemStackBuilder(Material.LIGHT_BLUE_WOOL)
                .name(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted").build());
        rankGrantAdmin.setItem(5, new ItemStackBuilder(Material.CYAN_WOOL)
                .name(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Moderator").build());
        rankGrantAdmin.setItem(6, new ItemStackBuilder(Material.BLUE_WOOL)
                .name(ChatColor.BLUE + "" + ChatColor.BOLD + "Administrator").build());
        rankGrantAdmin.setItem(7, null);
        rankGrantAdmin.setItem(8, null);
        return rankGrantAdmin;
    }

    public static Inventory getGrantInventoryMod(VLPlayer target) {
        Inventory rankGrantMod = Bukkit.createInventory(null, 9,
                ChatColor.DARK_GRAY + "Grant Rank to " + target.getName());
        rankGrantMod.setItem(0, null);
        rankGrantMod.setItem(1, null);
        rankGrantMod.setItem(2, new ItemStackBuilder(Material.LIGHT_GRAY_WOOL)
                .name(ChatColor.GRAY + "" + ChatColor.BOLD + "Member").build());
        rankGrantMod.setItem(3, new ItemStackBuilder(Material.WHITE_WOOL)
                .name(ChatColor.WHITE + "" + ChatColor.BOLD + "Patreon").build());
        rankGrantMod.setItem(4, new ItemStackBuilder(Material.LIGHT_BLUE_WOOL)
                .name(ChatColor.AQUA + "" + ChatColor.BOLD + "Trusted").build());
        rankGrantMod.setItem(5,
                new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).name(ChatColor.RED + "No Permission").build());
        rankGrantMod.setItem(6,
                new ItemStackBuilder(Material.RED_STAINED_GLASS_PANE).name(ChatColor.RED + "No Permission").build());
        rankGrantMod.setItem(7, null);
        rankGrantMod.setItem(8, null);
        return rankGrantMod;
    }
}