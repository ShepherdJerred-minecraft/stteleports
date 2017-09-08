package com.shepherdjerred.stteleports.controllers;

import com.shepherdjerred.stteleports.database.TeleportPlayerDAO;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TeleportController {

    private final TeleportPlayers teleportPlayers;
    private final TeleportPlayerDAO teleportPlayerDAO;
    private final Economy economy;

    public TeleportController(TeleportPlayers teleportPlayers, TeleportPlayerDAO teleportPlayerDAO, Economy economy) {
        this.teleportPlayers = teleportPlayers;
        this.teleportPlayerDAO = teleportPlayerDAO;
        this.economy = economy;
    }

    public void teleport(Teleport type, Player target, Player destination) {
        target.teleport(destination);
        if (!target.hasPermission("stTeleports.cooldown.exempt")) {
            doCooldown(teleportPlayers.get(target), type);
        }
        if (!target.hasPermission("stTeleports.cost.exempt")) {
            chargePlayer(target, type);
        }
    }

    public void teleport(Teleport type, Player target, Location destination, boolean safe) {

        if (safe) {
            // Let's be sure we don't teleport them in a block
            while (destination.getBlock().getType() != Material.AIR) {
                destination.add(0, 1, 0);
            }

            if (target.getGameMode() == GameMode.SURVIVAL) {
                // Let's be sure they don't fall to their death
                while (destination.add(0, -1, 0).getBlock().getType() == Material.AIR) {
                    destination.add(0, -1, 0);
                }
            }
        }

        target.teleport(destination);
        if (!target.hasPermission("stTeleports.cooldown.exempt")) {
            doCooldown(teleportPlayers.get(target), type);
        }
        if (!target.hasPermission("stTeleports.cost.exempt")) {
            chargePlayer(target, type);
        }
    }

    public void sendTeleportRequest(Player target, Player destination, Teleport type) {
        TeleportPlayer targetTpPlayer = teleportPlayers.get(target);
        TeleportPlayer destinationTpPlayer = teleportPlayers.get(destination);

        destinationTpPlayer.addRequest(targetTpPlayer.getUuid(), type);
        // TODO Expire the invite after some time
    }

    private void doCooldown(TeleportPlayer teleportPlayer, Teleport type) {
        long newCooldown = teleportPlayer.calculateCooldown(type.getCooldown());
        double newCooldownMultiplier = teleportPlayer.calculateCooldownMultiplier(type.getCooldownMultiplier());

        teleportPlayer.setCooldown(newCooldown);
        teleportPlayer.setCooldownMultiplier(newCooldownMultiplier);

        teleportPlayerDAO.updateCooldown(teleportPlayer);
        teleportPlayerDAO.updateCooldownMultiplier(teleportPlayer);
    }

    private void chargePlayer(Player player, Teleport type) {
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);
        if (economy != null) {
            economy.withdrawPlayer(player, type.getCost());
            double newCostMultiplier = teleportPlayer.calculateCostMultiplier(teleportPlayer.getCooldownMultiplier());
            teleportPlayer.setCostMultiplier(newCostMultiplier);
            teleportPlayerDAO.updateCostMultiplier(teleportPlayer);
        }
    }

}
