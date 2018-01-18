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

public class MisplaceCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "rail", permission = "meme.misplace")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /rail <player> <value>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        try {
            final double misplace = Double.parseDouble(args[1]);
            final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
            targetData.setMisplace(misplace);
            player.sendMessage(ChatColor.GREEN + target.getName() + "''s misplace value set to " + misplace + ".");
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "That's not a correct value.");
        }
    }

}
