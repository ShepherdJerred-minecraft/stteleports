package com.shepherdjerred.stteleports.commands;

import com.shepherdjerred.riotbase.commands.CommandNode;
import com.shepherdjerred.riotbase.commands.NodeInfo;
import com.shepherdjerred.riotbase.commands.NodeRegister;
import com.shepherdjerred.riotbase.commands.SpigotCommandSource;
import com.shepherdjerred.stteleports.database.TeleportPlayerDAO;
import com.shepherdjerred.stteleports.objects.TeleportPlayer;
import com.shepherdjerred.stteleports.objects.trackers.TeleportPlayers;
import org.bukkit.entity.Player;

public class SetHomeCommand extends CommandNode {

    private final TeleportPlayers teleportPlayers;
    private final TeleportPlayerDAO teleportPlayerDAO;

    public SetHomeCommand(NodeRegister nodeRegister, TeleportPlayers teleportPlayers, TeleportPlayerDAO teleportPlayerDAO) {
        super(nodeRegister, new NodeInfo(
                "sethome",
                "stTeleports.home.set",
                "Set your home",
                "/sethome [name]",
                0,
                false
        ));
        this.teleportPlayers = teleportPlayers;
        this.teleportPlayerDAO = teleportPlayerDAO;
    }

    @Override
    public void execute(SpigotCommandSource sender, String[] args) {

        String home = "default";

        if (args.length > 0) {
            if (args[0].length() < 3 || args[0].length() > 16) {
                sender.sendMessage(parser.colorString(true, "sethome.invalidLength"));
                return;
            }
            home = args[0];
        }

        Player player = sender.getPlayer();
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);

        boolean replaceExistingHome = false;

        if (teleportPlayer.getCurrentHomeCount() >= teleportPlayer.getMaxHomes()) {
            if (teleportPlayer.getHome(home) == null) {
                sender.sendMessage(parser.colorString(true, "sethome.tooManyHomes", teleportPlayer.getCurrentHomeCount(), teleportPlayer.getMaxHomes()));
                return;
            } else {
                replaceExistingHome = true;
            }
        }

        teleportPlayer.addHome(home, (sender.getPlayer()).getLocation());

        if (replaceExistingHome) {
            teleportPlayerDAO.updateHome(teleportPlayer, home);
        } else {
            teleportPlayerDAO.addHome(teleportPlayer, home);
        }

        sender.sendMessage(parser.colorString(true, "sethome.success"));

    }

}
