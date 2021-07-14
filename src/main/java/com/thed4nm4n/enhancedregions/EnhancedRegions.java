/*
Copyright 2021 Daniel Poe

This file is a part of EnhancedRegions. EnhancedRegions is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

EnhancedRegions is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with EnhancedRegions. If not, see
https://www.gnu.org/licenses/.
 */

package com.thed4nm4n.enhancedregions;

// All required imports. Check pom.xml for repositories necessary to build this plugin yourself.
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.flags.LocationFlag;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

// Java default library imports.
import java.util.Objects;
import java.util.logging.Logger;

public final class EnhancedRegions extends JavaPlugin implements Listener {

    public static StateFlag keepInventory;
    public static StateFlag dropExperience;
    public static LocationFlag teleportCoordinates;
    public static StringFlag teleportMessage;

    public static Logger logger;

    public void onLoad() {

        /*
        What this method does:

        Gets WorldGuard instance's flag registry.

        Registers 3 flags with WorldGuard: "keep-inventory", "teleport-coordinates", and "message-on-teleport".

        Sets static variables to registered flags for later use.

        Sets the logger static variable with the default logger.

        Sends a log message to confirm flag registration.
         */

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        StateFlag keepInvFlag = new StateFlag("keep-inventory", false);
        StateFlag dropExpFlag = new StateFlag("drop-experience", false);
        LocationFlag teleportCoordinatesFlag = new LocationFlag("teleport-coordinates", null);
        StringFlag teleportMessageFlag = new StringFlag("message-on-teleport");

        registry.register(keepInvFlag);
        registry.register(dropExpFlag);
        registry.register(teleportCoordinatesFlag);
        registry.register(teleportMessageFlag);

        keepInventory = keepInvFlag;
        dropExperience = dropExpFlag;
        teleportCoordinates = teleportCoordinatesFlag;
        teleportMessage = teleportMessageFlag;

        logger = getLogger();

        logger.info("Registered custom WorldGuard flags.");
    }

    public void onEnable() {

        /*
        What this method does:

        Registers events with Bukkit's plugin manager.
         */

        Bukkit.getPluginManager().registerEvents(this, this);

        logger.info("Listener events registered.");
    }

    public void onDisable() {}

    @EventHandler
    public void keepInventoryFlagHandler(PlayerDeathEvent event) {

        /*
        What this method does:

        Gets player and their location.

        Gets region container and all regions within the current world.

        Cycles through all applicable regions and checks if any of them has the "keep-inventory" flag set to ALLOW.

        If one does, it will set keep inventory to true, set keep level to true, and clear all drops.
         */

        Player player = event.getEntity();
        Location loc = player.getLocation();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regions != null) {

            for (ProtectedRegion r : regions.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))) {

                if (Objects.equals(r.getFlag((keepInventory)), StateFlag.State.ALLOW)) {

                    event.setKeepInventory(true);
                    event.setKeepLevel(true);
                    event.getDrops().clear();

                }
            }
        }
    }

    @EventHandler
    public void dropExperienceFlagHandler(PlayerDeathEvent event) {

        /*
        What this method does:

        Gets player and their location.

        Gets region container and all regions within the current world.

        Cycles through all applicable regions and checks if any of them has the "keep-inventory" flag set to ALLOW.

        If one does, it will set keep inventory to true, set keep level to true, and clear all drops.
         */

        Player player = event.getEntity();
        Location loc = player.getLocation();

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regions != null) {

            for (ProtectedRegion r : regions.getApplicableRegions(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))) {

                if (Objects.equals(r.getFlag((dropExperience)), StateFlag.State.DENY)) {

                    event.setDroppedExp(0);

                }
            }
        }
    }

    @EventHandler
    public void teleportOnRegionEnterHandler(RegionEnteredEvent event) {

        /*
        What this method does:

        Gets player and returns if player is null.

        Gets region that was entered.

        Gets the "teleport-coordinates" and "message-on-teleport" flags for the region entered.

        If "teleport-coordinates" exists, teleport the player to location specified.

        If "teleport-message" exists, send the player the message.
         */

        Player player = event.getPlayer();
        if (player == null) {return;}

        ProtectedRegion region = event.getRegion();

        com.sk89q.worldedit.util.Location teleportCoordinatesFlag = region.getFlag(teleportCoordinates);
        String messageFlag = region.getFlag(teleportMessage);

        if (teleportCoordinatesFlag != null) {
            player.setVelocity(new Vector());
            player.teleport(BukkitAdapter.adapt(teleportCoordinatesFlag));

            if (messageFlag != null) {
                player.sendMessage(messageFlag);
            }
        }
    }
}

