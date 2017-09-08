package com.shepherdjerred.stteleports.commands.registers;

import com.shepherdjerred.riotbase.commands.NodeRegister;
import com.shepherdjerred.stteleports.controllers.TeleportController;
import com.shepherdjerred.stteleports.messages.Parser;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import com.shepherdjerred.stteleports.vault.VaultManager;

public class TeleportNodeRegister extends NodeRegister {

    private final TeleportPlayers teleportPlayers;
    private final TeleportController teleportController;
    private final VaultManager vaultManager;

    public TeleportNodeRegister(Parser parser, TeleportPlayers teleportPlayers, TeleportController teleportController, VaultManager vaultManager) {
        super(parser);
        this.teleportPlayers = teleportPlayers;
        this.teleportController = teleportController;
        this.vaultManager = vaultManager;
    }

    public TeleportPlayers getTeleportPlayers() {
        return teleportPlayers;
    }

    public TeleportController getTeleportController() {
        return teleportController;
    }

    public VaultManager getVaultManager() {
        return vaultManager;
    }
}
