package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.rank.Rank;

public class AlertsCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "stfu", permission = Rank.ADMINISTRATOR)
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        this.plugin.getAlertsManager().toggleAlerts(player);
        player.sendMessage(this.plugin.getAlertsManager().hasAlertsToggled(player) ? (ChatColor.GREEN + "Subscribed to anticheat alerts.") : (ChatColor.RED + "Unsubscribed from anticheat alerts."));

    }

}
