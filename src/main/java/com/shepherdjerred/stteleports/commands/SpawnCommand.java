package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SpawnCommand extends AbstractTeleportCommand {

    public SpawnCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "spawn",
                "stTeleports.spawn",
                "Go to the worlds spawn",
                "/spawn",
                0,
                false
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player player = sender.getPlayer();
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (vaultManager.getEconomy() != null) {
            if (!vaultManager.getEconomy().has(player, Teleport.SPAWN.getCost())) {
                return;
            }
        }

        // TODO Make world name configurable
        teleportController.teleport(Teleport.SPAWN, player, Bukkit.getWorld("world").getSpawnLocation(), false);

        sender.sendMessage(parser.colorString(true, "spawn.success"));
    }

}
