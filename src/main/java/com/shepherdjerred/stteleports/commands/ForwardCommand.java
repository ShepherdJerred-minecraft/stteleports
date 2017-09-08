package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ForwardCommand extends AbstractTeleportCommand {

    public ForwardCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "forward",
                "stTeleports.forward",
                "Go forward in your teleport queue",
                "/forward",
                0,
                false,
                Arrays.asList("fw")
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player player = sender.getPlayer();
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);

        if (teleportPlayer.getFirstLocation() == null) {
            sender.sendMessage(parser.colorString(true, "player.emptyQueue"));
            return;
        }

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (vaultManager.getEconomy() != null) {
            if (!vaultManager.getEconomy().has(player, Teleport.FORWARD.getCost())) {
                return;
            }
        }

        teleportController.teleport(Teleport.FORWARD, player, teleportPlayer.getFirstLocation(), false);
        sender.sendMessage(parser.colorString(true, "forward.success"));

    }

}
