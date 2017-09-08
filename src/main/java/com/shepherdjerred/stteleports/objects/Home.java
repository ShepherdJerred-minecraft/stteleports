package com.shepherdjerred.stteleports.objects;

import org.bukkit.Location;
import org.bukkit.World;

public class Home {

    private final String name;
    private final Location location;

    public Home(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Home(String name, World world, int x, int y, int z, float yaw, float pitch) {
        this.name = name;
        this.location = new Location(world, x, y, z, yaw, pitch);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
