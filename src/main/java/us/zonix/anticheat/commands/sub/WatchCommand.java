package us.zonix.anticheat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;

public class WatchCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "skid", permission = "meme.watch")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /skid <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
        targetData.togglePlayerWatching(player);

        player.sendMessage(ChatColor.GREEN + "You are " + (targetData.isPlayerWatching(player) ? "now" : "no longer") + " focusing on " + player.getName() + " anticheat alerts.");
    }

}
