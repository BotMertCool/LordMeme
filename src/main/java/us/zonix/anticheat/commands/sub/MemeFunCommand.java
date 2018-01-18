package us.zonix.anticheat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;

public class MemeFunCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "meme", aliases = {"lordmeme"})
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "* " + ChatColor.GREEN + "Server is running Lord_MeMe version " + ChatColor.RED + "¯\\_(ツ)_/¯");
        }
    }

}
