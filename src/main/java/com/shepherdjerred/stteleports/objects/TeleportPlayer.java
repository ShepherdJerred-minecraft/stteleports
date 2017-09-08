package com.shepherdjerred.stteleports.objects;

import com.shepherdjerred.stteleports.util.TeleportQueue;
import org.bukkit.Location;

import java.util.*;

public class TeleportPlayer {

    private final UUID uuid;
    private final Map<String, Location> homes = new HashMap<>();
    private final Deque<Location> locations = new TeleportQueue<>(5);
    private final Map<UUID, Teleport> requesters = new HashMap<>();
    private long cooldown = 0;
    private double cooldownMultiplier = 0;
    private double costMultiplier = 0;

    private double cooldownMultiplierModifier = 0;
    private double costMultiplierModifier = 0;

    private int maxHomes = 1;

    public TeleportPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public TeleportPlayer(UUID uuid, int maxHomes, long cooldown, double cooldownMultiplier, double costMultiplier, double cooldownMultiplierModifier, double costMultiplierModifier) {
        this.uuid = uuid;
        this.maxHomes = maxHomes;
        this.cooldown = cooldown;
        this.cooldownMultiplier = cooldownMultiplier;
        this.costMultiplier = costMultiplier;
        this.cooldownMultiplierModifier = cooldownMultiplierModifier;
        this.costMultiplierModifier = costMultiplierModifier;
    }

    public double calculateCooldownMultiplier(double multiplier) {
        return cooldownMultiplier = cooldownMultiplier + (multiplier * cooldownMultiplierModifier);
    }

    public double calculateCostMultiplier(double multiplier) {
        return costMultiplier = costMultiplier + (multiplier * costMultiplierModifier);
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public double getCooldownMultiplier() {
        return cooldownMultiplier;
    }

    public void setCooldownMultiplier(double cooldownMultiplier) {
        this.cooldownMultiplier = cooldownMultiplier;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public double getCooldownMultiplierModifier() {
        return cooldownMultiplierModifier;
    }

    public void setCooldownMultiplierModifier(double cooldownMultiplierModifier) {
        this.cooldownMultiplierModifier = cooldownMultiplierModifier;
    }

    public double getCostMultiplierModifier() {
        return costMultiplierModifier;
    }

    public void setCostMultiplierModifier(double costMultiplierModifier) {
        this.costMultiplierModifier = costMultiplierModifier;
    }

    public void addRequest(UUID player, Teleport type) {
        requesters.put(player, type);
    }

    public boolean hasRequest(UUID player) {
        return requesters.containsKey(player);
    }

    public Teleport getTeleportType(UUID player) {
        return requesters.get(player);
    }

    public void removeRequest(UUID player) {
        requesters.remove(player);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getHome(String homeName) {
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(homeName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addHome(String name, Location location) {
        homes.put(name, location);
    }

    public void removeHome(String name) {
        homes.remove(name);
    }

    public Location getLastLocation() {
        return locations.removeLast();
    }

    public Location getFirstLocation() {
        return locations.removeFirst();
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public long calculateCooldown(long cooldown) {
        Calendar calendar = Calendar.getInstance();
        double newCooldown = ((cooldown * cooldownMultiplier) + cooldown) + calendar.getTimeInMillis();
        return (long) newCooldown;
    }

    public long getRemainingCooldown() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis() - cooldown;
    }

    public boolean isCooldownOver() {
        Calendar calendar = Calendar.getInstance();
        return cooldown <= calendar.getTimeInMillis();
    }

    public int getMaxHomes() {
        return maxHomes;
    }

    public void setMaxHomes(int maxHomes) {
        this.maxHomes = maxHomes;
    }

    public int getCurrentHomeCount() {
        return homes.size();
    }


    @Override
    public String toString() {
        return "TeleportPlayer{" +
                "uuid=" + uuid +
                ", cooldown=" + cooldown +
                ", homes=" + homes +
                ", locations=" + locations +
                ", requesters=" + requesters +
                ", cooldownMultiplier=" + cooldownMultiplier +
                ", costMultiplier=" + costMultiplier +
                ", cooldownMultiplierModifier=" + cooldownMultiplierModifier +
                ", costMultiplierModifier=" + costMultiplierModifier +
                ", maxHomes=" + maxHomes +
                '}';
    }
}
