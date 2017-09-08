package com.shepherdjerred.stteleports.config;

import com.shepherdjerred.riotbase.config.Config;
import org.bukkit.configuration.file.FileConfiguration;

public class SpigotTeleportsConfig implements Config, TeleportsConfig {

    private final FileConfiguration config;

    public SpigotTeleportsConfig(FileConfiguration config) {
        this.config = config;
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("debug");
    }

    public boolean isVaultEnabled() {
        return config.getBoolean("vault");
    }

}
