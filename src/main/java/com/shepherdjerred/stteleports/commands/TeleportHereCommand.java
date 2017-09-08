package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeleportHereCommand extends AbstractTeleportCommand {

    public TeleportHereCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "tphere",
                "stTeleports.tphere",
                "Teleport another player to you",
                "/tphere <target>",
                1,
                false,
                Arrays.asList("tph", "bring")
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        Player destination = sender.getPlayer();

        if (target == null) {
            sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[0]));
            return;
        }

        if (destination == target) {
            sender.sendMessage(parser.colorString(true, "generic.cantTargetSelf"));
            return;
        }

        TeleportPlayer teleportPlayer = teleportPlayers.get(target);

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (vaultManager.getEconomy() != null) {
            if (!vaultManager.getEconomy().has(target, Teleport.TPHERE.getCost())) {
                return;
            }
        }

        teleportController.teleport(Teleport.TPHERE, target, destination);
        sender.sendMessage(parser.colorString(true, "teleportHere.success", target.getName()));
        target.sendMessage(parser.colorString(true, "teleportHere.success.target", sender.getSender().getName()));
    }


}
