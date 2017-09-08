package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.commands.registers.TeleportNodeRegister;
import com.shepherdjerred.stteleports.objects.Teleport;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.util.TimeToString;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TeleportPositionCommand extends AbstractTeleportCommand {

    public TeleportPositionCommand(TeleportNodeRegister teleportNodeRegister) {
        super(teleportNodeRegister, new NodeInfo(
                "tppos",
                "stTeleports.tppos",
                "Teleport to a coordinate",
                "/tppos <x> <y> <z> [world]",
                3,
                false,
                Arrays.asList("tpp")
        ));
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {
        Player target = sender.getPlayer();
        TeleportPlayer teleportPlayer = teleportPlayers.get(target);
        double x;
        double y;
        double z;
        World world;

        if (!StringUtils.isNumeric(args[0])) {
            sender.sendMessage(parser.colorString(true, "generic.argumentMustBeNumber", args[0]));
            return;
        }

        if (!StringUtils.isNumeric(args[1])) {
            sender.sendMessage(parser.colorString(true, "generic.argumentMustBeNumber", args[1]));
            return;
        }

        if (!StringUtils.isNumeric(args[2])) {
            sender.sendMessage(parser.colorString(true, "generic.argumentMustBeNumber", args[2]));
            return;
        }

        if (args.length > 3) {
            world = Bukkit.getWorld(args[3]);
            if (world == null) {
                sender.sendMessage(parser.colorString(true, "generic.invalidWorld", args[3]));
                return;
            }
        } else {
            world = target.getWorld();
        }

        if (!teleportPlayer.isCooldownOver()) {
            sender.sendMessage(parser.colorString(true, "generic.cooldownActive", TimeToString.convertLong(teleportPlayer.getCooldown())));
            return;
        }

        if (vaultManager.getEconomy() != null) {
            if (!vaultManager.getEconomy().has(target, Teleport.TPPOS.getCost())) {
                return;
            }
        }

        x = Double.valueOf(args[0]);
        y = Double.valueOf(args[1]);
        z = Double.valueOf(args[2]);

        Location destination = new Location(world, x, y, z);

        teleportController.teleport(Teleport.TPPOS, target, destination, true);

        sender.sendMessage(parser.colorString(true, "tppos.success",
                target.getLocation().getBlockX(),
                target.getLocation().getBlockY(),
                target.getLocation().getBlockZ(),
                world.getName())
        );

    }

}
