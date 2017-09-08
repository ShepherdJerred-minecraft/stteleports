package com.shepherdjerred.stteleports.database;

import com.shepherdjerred.stteleports.objects.Home;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeleportPlayerDAO {

    private final FluentJdbc fluentJdbc;

    public TeleportPlayerDAO(FluentJdbc fluentJdbc) {
        this.fluentJdbc = fluentJdbc;
    }

    public void updateCooldown(TeleportPlayer teleportPlayer) {
        System.out.println("Updating CD");
        Query query = fluentJdbc.query();
        query
                .update("UPDATE players SET cooldown = ? WHERE player_uuid = ?")
                .params(
                        teleportPlayer.getCooldown(),
                        teleportPlayer.getUuid()
                ).run();
    }

    public void updateCooldownMultiplier(TeleportPlayer teleportPlayer) {
        System.out.println("Updating CD Mult");
        Query query = fluentJdbc.query();
        query
                .update("UPDATE players SET cooldown_multiplier = ? WHERE player_uuid = ?")
                .params(
                        teleportPlayer.getCooldownMultiplier(),
                        teleportPlayer.getUuid()
                ).run();
    }

    public void updateCostMultiplier(TeleportPlayer teleportPlayer) {
        System.out.println("Updating Cost Mult");
        Query query = fluentJdbc.query();
        query
                .update("UPDATE players SET cost_multiplier = ? WHERE player_uuid = ?")
                .params(
                        teleportPlayer.getCostMultiplier(),
                        teleportPlayer.getUuid()
                ).run();
    }

    public void updateCooldownMultiplierModifier(TeleportPlayer teleportPlayer) {
        Query query = fluentJdbc.query();
        query
                .update("UPDATE players SET cooldown_multiplier_modifier = ? WHERE player_uuid = ?")
                .params(
                        teleportPlayer.getCooldownMultiplierModifier(),
                        teleportPlayer.getUuid()
                ).run();
    }

    public void updateCostMultiplierModifier(TeleportPlayer teleportPlayer) {
        Query query = fluentJdbc.query();
        query
                .update("UPDATE players SET cost_multiplier_modifier = ? WHERE player_uuid = ?")
                .params(
                        teleportPlayer.getCostMultiplierModifier(),
                        teleportPlayer.getUuid()
                ).run();
    }

    public void insert(TeleportPlayer player) {
        Query query = fluentJdbc.query();
        query
                .update("INSERT INTO players VALUES (?,?,?,?,?,?,?)")
                .params(
                        player.getUuid().toString(),
                        player.getMaxHomes(),
                        player.getCooldown(),
                        player.getCooldownMultiplier(),
                        player.getCostMultiplier(),
                        player.getCooldownMultiplierModifier(),
                        player.getCostMultiplierModifier()
                ).run();
    }

    public void addHome(TeleportPlayer player, String home) {
        Location location = player.getHome(home);

        Query query = fluentJdbc.query();
        query
                .update("INSERT INTO player_homes VALUES (?,?,?,?,?,?,?,?)")
                .params(
                        player.getUuid().toString(),
                        home,
                        location.getWorld().getUID().toString(),
                        location.getBlockX(),
                        location.getBlockY(),
                        location.getBlockZ(),
                        location.getYaw(),
                        location.getPitch()
                ).run();
    }

    public void updateHome(TeleportPlayer teleportPlayer, String home) {
        Location playerHomeLocation = teleportPlayer.getHome(home);
        Query query = fluentJdbc.query();
        query
                .update("UPDATE player_homes SET x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE player_uuid = ? AND name = ?")
                .params(
                        playerHomeLocation.getX(),
                        playerHomeLocation.getY(),
                        playerHomeLocation.getZ(),
                        playerHomeLocation.getYaw(),
                        playerHomeLocation.getPitch(),
                        String.valueOf(teleportPlayer.getUuid()),
                        home
                ).run();
    }

    public void deleteHome(TeleportPlayer player, String home) {
        Query query = fluentJdbc.query();
        query
                .update("DELETE FROM player_homes WHERE player_uuid = ? AND name = ?")
                .params(
                        player.getUuid().toString(),
                        home
                ).run();
    }

    private void loadHomes(TeleportPlayer player) {

        Mapper<Home> homeMapper = rs -> new Home(
                rs.getString("name"),
                Bukkit.getWorld(UUID.fromString(rs.getString("world"))),
                rs.getInt("x"),
                rs.getInt("y"),
                rs.getInt("z"),
                rs.getFloat("yaw"),
                rs.getFloat("pitch")
        );

        Query query = fluentJdbc.query();

        List<Home> locations = query
                .select("SELECT * FROM player_homes WHERE player_uuid = ?")
                .params(
                        player.getUuid().toString()
                ).listResult(homeMapper);

        locations.forEach(home -> {
            player.addHome(home.getName(), home.getLocation());
            System.out.println(home.getLocation().getWorld().getName());
        });

    }

    public TeleportPlayer load(UUID uuid) {

        Mapper<TeleportPlayer> teleportPlayerMapper = rs -> new TeleportPlayer(
                uuid,
                rs.getInt("max_homes"),
                rs.getLong("cooldown"),
                rs.getDouble("cooldown_multiplier"),
                rs.getDouble("cost_multiplier"),
                rs.getDouble("cooldown_multiplier_modifier"),
                rs.getDouble("cost_multiplier_modifier")
        );

        Query query = fluentJdbc.query();

        Optional<TeleportPlayer> teleportPlayer = query
                .select("SELECT * FROM players WHERE player_uuid = ?")
                .params(
                        uuid.toString()
                ).firstResult(teleportPlayerMapper);

        if (teleportPlayer.isPresent()) {
            loadHomes(teleportPlayer.get());
            return teleportPlayer.get();
        } else {
            return null;
        }

    }

}
