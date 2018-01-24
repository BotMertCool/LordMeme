package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

public class RangeCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "setrangevl", permission = Rank.DEVELOPER)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /setrangevl <volume>");
            return;
        }

        try {
            final double volume = Double.parseDouble(args[0]);
            this.plugin.setRangeVl(volume);

            player.sendMessage(ChatColor.GREEN + "Range volume has been set to " + volume);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "That's not a correct value.");
        }
    }

}
