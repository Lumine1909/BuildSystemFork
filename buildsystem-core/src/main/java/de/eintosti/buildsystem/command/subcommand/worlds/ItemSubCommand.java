/*
 * Copyright (c) 2018-2025, Thomas Meaney
 * Copyright (c) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.eintosti.buildsystem.command.subcommand.worlds;

import de.eintosti.buildsystem.BuildSystem;
import de.eintosti.buildsystem.Messages;
import de.eintosti.buildsystem.command.subcommand.Argument;
import de.eintosti.buildsystem.command.subcommand.SubCommand;
import de.eintosti.buildsystem.tabcomplete.WorldsTabComplete;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemSubCommand implements SubCommand {

    private final BuildSystem plugin;

    public ItemSubCommand(BuildSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!hasPermission(player)) {
            plugin.sendPermissionMessage(player);
            return;
        }

        ItemStack navigator = plugin.getInventoryUtil().getItemStack(
            plugin.getConfigValues().getNavigatorItem(),
            Messages.getString("navigator_item", player)
        );
        player.getInventory().addItem(navigator);
        Messages.sendMessage(player, "worlds_item_receive");
    }

    @Override
    public Argument getArgument() {
        return WorldsTabComplete.WorldsArgument.ITEM;
    }
}