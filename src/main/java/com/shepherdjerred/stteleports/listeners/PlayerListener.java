package com.shepherdjerred.stteleports.listeners;

import com.shepherdjerred.stteleports.database.TeleportPlayerDAO;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final TeleportPlayers teleportPlayers;
    private final TeleportPlayerDAO teleportPlayerDAO;

    public PlayerListener(TeleportPlayers teleportPlayers, TeleportPlayerDAO teleportPlayerDAO) {
        this.teleportPlayers = teleportPlayers;
        this.teleportPlayerDAO = teleportPlayerDAO;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // TODO Do this async
        loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        teleportPlayers.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        TeleportPlayer player = teleportPlayers.get(event.getPlayer());
        if (player != null) {
            player.addLocation(event.getFrom());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        TeleportPlayer player = teleportPlayers.get(event.getEntity());
        if (player != null) {
            player.addLocation(event.getEntity().getLocation());
        }
    }

    public void loadPlayer(UUID uuid) {
        TeleportPlayer teleportPlayer = teleportPlayerDAO.load(uuid);

        if (teleportPlayer == null) {
            teleportPlayer = new TeleportPlayer(uuid);
            teleportPlayerDAO.insert(teleportPlayer);
        }

        teleportPlayers.add(teleportPlayer);
    }

}
