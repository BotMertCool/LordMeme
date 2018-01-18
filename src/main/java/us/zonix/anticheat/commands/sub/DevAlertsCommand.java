package us.zonix.anticheat.commands.sub;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;

public class DevAlertsCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "gtfo", permission = "meme.devalerts")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        this.plugin.getAlertsManager().toggleDevAlerts(player);
        player.sendMessage(this.plugin.getAlertsManager().hasDevAlertsToggled(player) ? (ChatColor.GREEN + "Subscribed to anticheat dev alerts.") : (ChatColor.RED + "Unsubscribed from anticheat dev alerts."));

    }

}
