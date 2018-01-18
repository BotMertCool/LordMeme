package us.zonix.anticheat.commands.sub;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.requests.MemeFetchRequest;
import us.zonix.anticheat.util.command.Command;
import us.zonix.anticheat.util.command.CommandArgs;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.callback.AbstractBukkitCallback;

public class LogsCommand extends MemeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    @Command(name = "logs", permission = "meme.logs")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /logs <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");

        CorePlugin.getInstance().getRequestProcessor().sendRequestAsync(
                new MemeFetchRequest.FetchByUuidRequest(target.getUniqueId()),
                new AbstractBukkitCallback() {
                    @Override
                    public void callback(JsonElement jsonElement) {
                        if (!jsonElement.isJsonNull()) {
                            JsonObject stats = jsonElement.getAsJsonObject();

                            JsonElement element = stats.get("message");

                            if (element != null && !element.isJsonNull()) {
                                player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "* " + ChatColor.GOLD + element.getAsString());
                            }


                        }
                    }

                    @Override
                    public void onError(String message) {
                        super.onError(message);
                        LogsCommand.this.plugin.getLogger().severe("Error fetching practice stats for " + target.getName());
                    }
                }
        );

        player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
    }

}
