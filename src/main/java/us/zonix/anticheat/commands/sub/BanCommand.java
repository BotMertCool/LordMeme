package us.zonix.anticheat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerBanEvent;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

public class BanCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "rape", permission = Rank.ADMINISTRATOR)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /rape <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(target);
        playerData.setBanning(true);
        final PlayerBanEvent event = new PlayerBanEvent(target, ChatColor.YELLOW + "was banned by " + ChatColor.GOLD + player.getName() + ".");
        this.plugin.getServer().getPluginManager().callEvent((Event)event);
    }

}
