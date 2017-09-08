package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.CommandNode;
import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.controllers.TeleportController;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import com.shepherdjerred.stteleports.vault.VaultManager;

public abstract class AbstractTeleportCommand extends CommandNode {

    protected final TeleportPlayers teleportPlayers;
    protected final TeleportController teleportController;
    protected final VaultManager vaultManager;

    public AbstractTeleportCommand(TeleportNodeRegister register, NodeInfo nodeInfo, AbstractTeleportCommand... children) {
        super(register, nodeInfo, children);
        this.teleportPlayers = register.getTeleportPlayers();
        this.teleportController = register.getTeleportController();
        this.vaultManager = register.getVaultManager();
    }

}
