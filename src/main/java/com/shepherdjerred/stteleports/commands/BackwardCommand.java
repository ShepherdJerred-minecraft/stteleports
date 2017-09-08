package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BackwardCommand extends AbstractTeleportCommand {

    public BackwardCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "backward",
                "stTeleports.backward",
                "Go backward in your teleport queue",
                "/backward",
                0,
                false,
                Arrays.asList("back", "return")
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player player = sender.getPlayer();
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);

        if (teleportPlayer.getLastLocation() == null) {
            sender.sendMessage(parser.colorString(true, "player.emptyQueue"));
            return;
        }

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (vaultManager.getEconomy() != null) {
            if (!vaultManager.getEconomy().has(player, Teleport.BACKWARD.getCost())) {
                return;
            }
        }

        teleportController.teleport(Teleport.BACKWARD, player, teleportPlayer.getLastLocation(), false);
        sender.sendMessage(parser.colorString(true, "backward.success"));

    }

}
