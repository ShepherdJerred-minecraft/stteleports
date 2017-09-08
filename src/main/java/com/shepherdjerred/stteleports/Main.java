package com.shepherdjerred.stteleports;

import com.shepherdjerred.riotbase.RiotBase;
import com.shepherdjerred.riotbase.commands.NodeRegister;
import com.shepherdjerred.stteleports.commands.*;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.commands.tpa.TpaCommand;
import com.shepherdjerred.stteleports.commands.tpa.TpaHereCommand;
import com.shepherdjerred.stteleports.config.SpigotTeleportsConfig;
import com.shepherdjerred.stteleports.controllers.TeleportController;
import com.shepherdjerred.stteleports.database.TeleportPlayerDAO;
import com.shepherdjerred.stteleports.listeners.PlayerListener;
import com.shepherdjerred.stteleports.messages.Parser;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import com.shepherdjerred.stteleports.vault.VaultManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.flywaydb.core.Flyway;

import java.util.ResourceBundle;

public class Main extends RiotBase {

    private Parser parser;

    private TeleportPlayers teleportPlayers;
    private TeleportController teleportController;

    private HikariDataSource hikariDataSource;
    private FluentJdbc fluentJdbc;
    private TeleportPlayerDAO teleportPlayerDAO;

    private VaultManager vaultManager;
    private SpigotTeleportsConfig spigotTeleportsConfig;

    @Override
    public void onEnable() {
        setupConfigs();
        setupDatabase();

        // TODO Load from filesystem
        parser = new Parser(ResourceBundle.getBundle("messages"));
        teleportPlayers = new TeleportPlayers();
        vaultManager = new VaultManager(this);
        teleportController = new TeleportController(teleportPlayers, teleportPlayerDAO, vaultManager.getEconomy());

        if (spigotTeleportsConfig.isVaultEnabled()) {
            vaultManager.setupEconomy();
        }

        registerCommands();
        registerListeners();

        checkOnlinePlayers();

        runCooldownReduction();

        startMetrics();
    }

    // TODO Move this to its own class
    private void runCooldownReduction() {

        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, () -> Bukkit.getOnlinePlayers().forEach(player -> {

            TeleportPlayer teleportPlayer = teleportPlayers.get(player);

            double currentCostMultiplier = teleportPlayer.getCostMultiplier();
            double currentCooldownMultiplier = teleportPlayer.getCooldownMultiplier();

            currentCostMultiplier -= .5;
            currentCooldownMultiplier -= .5;

            if (currentCostMultiplier < 0) {
                currentCostMultiplier = 0;
            }

            if (currentCooldownMultiplier < 0) {
                currentCooldownMultiplier = 0;
            }

            teleportPlayer.setCostMultiplier(currentCostMultiplier);
            teleportPlayer.setCooldownMultiplier(currentCooldownMultiplier);

        }), 0L, 12000L);

    }

    protected void setupConfigs() {
        copyFile(getResource("config.yml"), getDataFolder().getAbsolutePath() + "/config.yml");
        copyFile(getResource("messages.properties"), getDataFolder().getAbsolutePath() + "/messages.properties");

        spigotTeleportsConfig = new SpigotTeleportsConfig(getConfig());

        // TODO Load teleport settings from JSON
    }

    private void setupDatabase() {
        copyFile(getResource("hikari.properties"), getDataFolder().getAbsolutePath() + "/hikari.properties");
        copyFile(getResource("db/migration/V1__Initial.sql"), getDataFolder().getAbsolutePath() + "/db/migration/V1__Initial.sql");

        HikariConfig hikariConfig = new HikariConfig(getDataFolder().getAbsolutePath() + "/hikari.properties");
        hikariDataSource = new HikariDataSource(hikariConfig);

        fluentJdbc = new FluentJdbcBuilder().connectionProvider(hikariDataSource).build();

        Flyway flyway = new Flyway();
        flyway.setLocations("filesystem:" + getDataFolder().getAbsolutePath() + "/db/migration/");
        flyway.setDataSource(hikariDataSource);
        flyway.migrate();

        teleportPlayerDAO = new TeleportPlayerDAO(fluentJdbc);
    }

    private void registerCommands() {
        NodeRegister nr = new NodeRegister(parser);
        nr.addNode(new SetHomeCommand(nr, teleportPlayers, teleportPlayerDAO));
        nr.addNode(new DelHomeCommand(nr, teleportPlayers, teleportPlayerDAO));
        nr.registerCommandsForBukkit(this);

        TeleportNodeRegister tnr = new TeleportNodeRegister(parser, teleportPlayers, teleportController, vaultManager);
        tnr.addNode(new BackwardCommand(tnr));
        tnr.addNode(new ForwardCommand(tnr));
        tnr.addNode(new HomeCommand(tnr));
        tnr.addNode(new SpawnCommand(tnr));
        tnr.addNode(new TeleportCommand(tnr));
        tnr.addNode(new TeleportHereCommand(tnr));
        tnr.addNode(new TeleportPositionCommand(tnr));
        tnr.addNode(new TpaCommand(tnr));
        tnr.addNode(new TpaHereCommand(tnr));
        tnr.registerCommandsForBukkit(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(teleportPlayers, teleportPlayerDAO), this);
    }

    private void checkOnlinePlayers() {
        // Yeah, this is a bit hacky
        PlayerListener playerListener = new PlayerListener(teleportPlayers, teleportPlayerDAO);
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerListener.loadPlayer(player.getUniqueId());
        }
    }

}
