package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.log.LogRequestFecther;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

public class LogsCommand extends MemeCommand {

    @Command(name = "logs", permission = Rank.ADMINISTRATOR)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /logs <player>");
            return;
        }

        new BukkitRunnable() {
            public void run() {
                new LogRequestFecther(sender, args[0]);
            }
        }.runTaskAsynchronously(LordMeme.getInstance());
    }

}
