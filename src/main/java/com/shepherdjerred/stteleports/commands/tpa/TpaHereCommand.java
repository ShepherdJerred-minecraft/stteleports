package com.shepherdjerred.stteleports.commands.tpa;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.AbstractTeleportCommand;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TpaHereCommand extends AbstractTeleportCommand {

    public TpaHereCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "tpahere",
                "stTeleports.tpahere",
                "Request that another player teleports to you",
                "/tpahere <target>",
                1,
                false
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player target = sender.getPlayer();
        Player destination = Bukkit.getPlayer(args[0]);
        TeleportPlayer teleportPlayer = teleportPlayers.get(target);

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (destination == null) {
            sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[0]));
            return;
        }

        if (destination == target) {
            sender.sendMessage(parser.colorString(true, "generic.cantTargetSelf"));
            return;
        }

        teleportController.sendTeleportRequest(target, destination, Teleport.TPAHERE);

        sender.sendMessage(parser.colorString(true, "tpahere.send.success", destination.getName()));
        destination.sendMessage(parser.colorString(true, "tpahere.send.destination", sender.getSender().getName()));

    }

}
