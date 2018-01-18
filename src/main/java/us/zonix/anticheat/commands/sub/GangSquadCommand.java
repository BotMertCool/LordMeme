package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;

public class GangSquadCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "gangsquad", permission = "meme.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {

            player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
            player.sendMessage(ChatColor.RED + "Lord_Meme Commands:");
            player.sendMessage(" ");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/stfu - Regular alerts");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/gtfo - Developer alerts");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/rape - Ban someone");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/skid - Watch someone");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/pussy - Exempt someone");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/rail - Misplace command");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/setrangevl - Set range volume");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/kys - Toggle a check");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/banwave - Ban Wave executable");
            player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/logs - Display player logs");
            player.sendMessage(" ");
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "* " + ChatColor.GREEN + "Server is running Lord_MeMe version " + ChatColor.RED + "¯\\_(ツ)_/¯");
            player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
        }
    }

}
