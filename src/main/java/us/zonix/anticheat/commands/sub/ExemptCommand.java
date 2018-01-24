package us.zonix.anticheat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

public class ExemptCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "pussy", permission = Rank.MANAGER)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /pussy <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
        targetData.setBanning(!targetData.isBanning());
        player.sendMessage(ChatColor.GREEN + target.getName() + " is " + (targetData.isBanning() ? "no longer" : "now") + " getting banned by Lord_MeMe.");
    }

}
