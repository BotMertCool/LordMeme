package us.zonix.anticheat.log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.zonix.anticheat.requests.MemeFetchRequest;
import us.zonix.core.CorePlugin;
import us.zonix.core.profile.Profile;
import us.zonix.core.shared.api.callback.Callback;
import us.zonix.core.util.UUIDType;

import java.util.UUID;

public class LogRequestFecther {

    private CommandSender sender;

    private UUID uuid;
    private String name;

    public LogRequestFecther(CommandSender sender, String name) {
        this.sender = sender;
        this.name = name;

        this.getPlayerInformation((useless) -> {
            if (this.uuid == null || this.name == null) {
                sender.sendMessage(ChatColor.RED + "Failed to find that player.");
            }
            else {
                this.attempt();
            }
        });
    }

    private void getPlayerInformation(Callback callback) {
        Player player = Bukkit.getPlayer(this.name);

        if (player != null) {
            this.uuid = player.getUniqueId();
            this.name = player.getName();

            callback.callback(null);
        }
        else {
            this.sender.sendMessage(ChatColor.GRAY + "(Resolving player information...)");

            Profile.getPlayerInformation(this.name, this.sender, (retrieved) -> {
                if (retrieved != null) {
                    uuid = UUIDType.fromString(retrieved.getAsJsonObject().get("uuid").getAsString());
                }

                callback.callback(null);
            });
        }
    }

    private void attempt() {
        new BukkitRunnable() {
            public void run() {

                Profile profile = Profile.getByUuid(uuid);

                JsonElement response = CorePlugin.getInstance().getRequestProcessor().sendRequest(new MemeFetchRequest.FetchByUuidRequest(uuid));

                if (response.isJsonNull() || response.isJsonPrimitive()) {
                    System.out.println("Error while getting JSON response.");
                    System.out.println("Issue: " + response.toString());
                    return;
                }

                JsonArray data = response.getAsJsonArray();

                if(data.size() == 0) {
                    sender.sendMessage(ChatColor.RED + "That player doesn't have any logs.");
                }

                data.iterator().forEachRemaining((element) -> {
                    JsonObject object = element.getAsJsonObject();
                    String message = object.get("message").getAsString();
                    sender.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "* " + ChatColor.GOLD + message);
                });

            }
        }.runTaskAsynchronously(CorePlugin.getInstance());
    }

}
