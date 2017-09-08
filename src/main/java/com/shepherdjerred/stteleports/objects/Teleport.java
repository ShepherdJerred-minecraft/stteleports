package com.shepherdjerred.stteleports.objects;

public enum Teleport {
    TELEPORT, TPHERE, TPPOS, HOME, SPAWN, TPA, TPAHERE, BACKWARD, FORWARD;

    private long cooldown = 120000;
    private long cost = 25;
    private double cooldownMultiplier = 0.5;
    private double costMultiplier = 0.5;

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
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

}
