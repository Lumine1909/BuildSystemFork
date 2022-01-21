/*
 * Copyright (c) 2022, Thomas Meaney
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.eintosti.buildsystem.listener;

import com.cryptomorin.xseries.XMaterial;
import com.eintosti.buildsystem.BuildSystem;
import com.eintosti.buildsystem.manager.WorldManager;
import com.eintosti.buildsystem.object.world.BuildWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

/**
 * @author einTosti
 */
public class BlockPhysicsListener implements Listener {

    private final WorldManager worldManager;

    public BlockPhysicsListener(BuildSystem plugin) {
        this.worldManager = plugin.getWorldManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        World bukkitWorld = block.getWorld();
        BuildWorld buildWorld = worldManager.getBuildWorld(bukkitWorld.getName());

        if (buildWorld == null || buildWorld.isPhysics()) {
            return;
        }

        XMaterial xMaterial = XMaterial.matchXMaterial(block.getType());
        BlockFace[] surroundingBlocks = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        switch (xMaterial) {
            case REDSTONE_BLOCK:
                for (BlockFace blockFace : surroundingBlocks) {
                    if (isCustomRedstoneLamp(block.getRelative(blockFace))) {
                        event.setCancelled(false);
                        return;
                    }
                }
                break;
            case REDSTONE_LAMP:
                for (BlockFace blockFace : surroundingBlocks) {
                    if (block.getRelative(blockFace).getType() == XMaterial.REDSTONE_BLOCK.parseMaterial()) {
                        event.setCancelled(false);
                        return;
                    }
                }
                break;
        }

        event.setCancelled(true);
    }

    private boolean isCustomRedstoneLamp(Block block) {
        return block.getType().name().equals("REDSTONE_LAMP_ON");
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld != null && !buildWorld.isPhysics()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        BuildWorld buildWorld = worldManager.getBuildWorld(block.getWorld().getName());

        if (buildWorld == null || buildWorld.isPhysics()) {
            return;
        }

        if (event.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            event.setCancelled(true);
            event.getBlock().getState().update(false, false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockRedstone(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        XMaterial xMaterial = XMaterial.matchXMaterial(block.getType());

        if (isCustomRedstoneLamp(block)) {
            event.setNewCurrent(15);
        }

        if (xMaterial != XMaterial.REDSTONE_BLOCK) {
            return;
        }

        for (BlockFace blockFace : new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
            if (isCustomRedstoneLamp(block.getRelative(blockFace))) {
                event.setNewCurrent(15);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        Block block = event.getBlock();
        World bukkitWorld = block.getWorld();
        BuildWorld buildWorld = worldManager.getBuildWorld(bukkitWorld.getName());

        if (buildWorld != null && !buildWorld.isExplosions()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();
        World bukkitWorld = location.getWorld();
        BuildWorld buildWorld = worldManager.getBuildWorld(bukkitWorld.getName());

        if (buildWorld != null && !buildWorld.isExplosions()) {
            event.setCancelled(true);
        }
    }
}
