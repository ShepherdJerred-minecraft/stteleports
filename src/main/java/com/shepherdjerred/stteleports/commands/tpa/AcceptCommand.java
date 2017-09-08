package com.shepherdjerred.stteleports.commands.tpa;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.AbstractTeleportCommand;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AcceptCommand extends AbstractTeleportCommand {

    public AcceptCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "accept",
                "stTeleports.tpa.accept",
                "Accept a teleport request",
                "/tpa accept <player>",
                1,
                false
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        Player senderPlayer = sender.getPlayer();
        TeleportPlayer senderTeleportPlayer = teleportPlayers.get(senderPlayer);

        if (targetPlayer == null) {
            sender.sendMessage(parser.colorString(true, "generic.playerNotOnline", args[0]));
            return;
        }

        if (!senderTeleportPlayer.hasRequest(targetPlayer.getUniqueId())) {
            sender.sendMessage(parser.colorString(true, "tpa.noRequest", targetPlayer.getName()));
            return;
        }

        Teleport type = senderTeleportPlayer.getTeleportType(targetPlayer.getUniqueId());
        senderTeleportPlayer.removeRequest(targetPlayer.getUniqueId());

        if (type == Teleport.TPA) {
            teleportController.teleport(Teleport.TPA, targetPlayer, senderPlayer);
        } else if (type == Teleport.TPAHERE) {
            teleportController.teleport(Teleport.TPAHERE, senderPlayer, targetPlayer);
        } else {
            sender.sendMessage("Invalid teleport type. Please report this to an administrator");
            return;
        }

        senderPlayer.sendMessage(parser.colorString(true, "tpa.accept.sender", targetPlayer.getName()));
        targetPlayer.sendMessage(parser.colorString(true, "tpa.accept.target", senderPlayer.getName()));

    }

}
