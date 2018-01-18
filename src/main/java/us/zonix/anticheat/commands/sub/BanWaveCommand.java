package us.zonix.anticheat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.BanWaveEvent;
import us.zonix.anticheat.event.player.PlayerBanEvent;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;

public class BanWaveCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "banwave", permission = "meme.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /banwave <start/end>");
            return;
        }

        if(args[0].equalsIgnoreCase("start")) {

            if(this.plugin.getBanWaveManager().isBanWaveStarted()) {
                player.sendMessage(ChatColor.RED + "(Ban Wave) There is currently an active event.");
                return;
            }

            final BanWaveEvent event = new BanWaveEvent(player.getName());
            this.plugin.getServer().getPluginManager().callEvent(event);
            player.sendMessage(ChatColor.GREEN + "(Ban Wave) You just toggled the event to start.");
        }

        else if(args[0].equalsIgnoreCase("end")) {

            if(!this.plugin.getBanWaveManager().isBanWaveStarted()) {
                player.sendMessage(ChatColor.RED + "(Ban Wave) There is no event to end.");
                return;
            }

            this.plugin.getBanWaveManager().setBanWaveStarted(false);
            player.sendMessage(ChatColor.RED + "(Ban Wave) You just toggled the event to end.");
        }

    }

}
