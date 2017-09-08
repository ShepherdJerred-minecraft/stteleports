package com.shepherdjerred.stteleports.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultManager {

    private Economy economy;
    private final JavaPlugin javaPlugin;

    public VaultManager(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public void setupEconomy() {
        if (economy == null) {
            RegisteredServiceProvider<Economy> economyProvider = javaPlugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }
    }

    public Economy getEconomy() {
        return economy;
    }

}
