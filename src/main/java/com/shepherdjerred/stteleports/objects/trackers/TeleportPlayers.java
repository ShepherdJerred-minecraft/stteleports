package com.shepherdjerred.stteleports.objects.trackers;

import com.shepherdjerred.riotbase.commands.CommandSource;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeleportPlayers {

    private final Map<UUID, TeleportPlayer> playerTracker = new ConcurrentHashMap<>();

    public void remove(UUID uuid) {
        playerTracker.remove(uuid);
    }

    public void remove(TeleportPlayer entity) {
        playerTracker.remove(entity.getUuid());
    }

    public void add(TeleportPlayer entity) {
        playerTracker.put(entity.getUuid(), entity);
    }

    public void addAll(List<TeleportPlayer> entities) {
        for (TeleportPlayer entity : entities) {
            add(entity);
        }
    }

    public TeleportPlayer get(TeleportPlayer entity) {
        return playerTracker.get(entity.getUuid());
    }

    public TeleportPlayer get(UUID uuid) {
        return playerTracker.get(uuid);
    }

    public List<TeleportPlayer> getAsList() {
        return new ArrayList<>(playerTracker.values());
    }

    public boolean contains(UUID uuid) {
        return playerTracker.containsKey(uuid);
    }

    public boolean contains(TeleportPlayer entity) {
        return playerTracker.containsValue(entity);
    }

    public TeleportPlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public TeleportPlayer get(CommandSource sender) {
        if (sender instanceof Player) {
            return get((sender.getPlayer()).getUniqueId());
        }
        return null;
    }

    public void remove(Player player) {
        remove(get(player));
    }

}
