package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

import java.util.HashSet;
import java.util.Set;

public class ToggleCommand extends MemeCommand {

    public static final Set<String> DISABLED_CHECKS;
    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "kys", permission = Rank.DEVELOPER)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /kys <check>");
            return;
        }

        final String check = args[0].toUpperCase();

        if (!ToggleCommand.DISABLED_CHECKS.remove(check)) {
            ToggleCommand.DISABLED_CHECKS.add(check);
            player.sendMessage(ChatColor.RED + check +  " has been disabled.");
        }
        else {
            player.sendMessage(ChatColor.GREEN + check +  " has been enabled.");
        }
    }

    static {
        DISABLED_CHECKS = new HashSet<String>();
    }

}
