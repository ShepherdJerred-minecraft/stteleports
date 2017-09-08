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

public class TeleportCommand extends AbstractTeleportCommand {

    public TeleportCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "teleport",
                "stTeleports.teleport",
                "Teleport to another player",
                "/teleport [target] <destination>",
                1,
                false,
                Arrays.asList("tp")
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player target;
        Player destination;

        if (args.length > 1) {
            target = Bukkit.getPlayer(args[0]);
            destination = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[0]));
                return;
            }
            if (destination == null) {
                sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[1]));
                return;
            }
        } else {
            target = sender.getPlayer();
            destination = Bukkit.getPlayer(args[0]);

            if (destination == null) {
                sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[0]));
                return;
            }
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
            if (!vaultManager.getEconomy().has(target, Teleport.TELEPORT.getCost())) {
                return;
            }
        }

        teleportController.teleport(Teleport.TELEPORT, target, destination);

        if (args.length > 1) {
            // The sender teleported themself to someone
            if (target == sender.getPlayer()) {
                sender.sendMessage(parser.colorString(true, "teleport.success.sender", destination.getName()));
                destination.sendMessage(parser.colorString(true, "teleport.success.destination", sender.getName()));
                return;
            }

            // The sender teleported someone to them
            if (destination == sender) {
                target.sendMessage(parser.colorString(true, "teleportHere.success.target", sender.getName()));
                sender.sendMessage(parser.colorString(true, "teleportHere.success", target.getName()));
                return;
            }

            // The sender teleported someone to someone else
            sender.sendMessage(parser.colorString(true, "teleport.multiple.sender", target.getName(), destination.getName()));
            target.sendMessage(parser.colorString(true, "teleport.multiple.target", sender.getName(), destination.getName()));
            destination.sendMessage(parser.colorString(true, "teleport.multiple.destination", sender.getName(), target.getName()));
        } else {
            sender.sendMessage(parser.colorString(true, "teleport.success.sender", destination.getName()));
            destination.sendMessage(parser.colorString(true, "teleport.success.destination", sender.getName()));
        }

    }

}
