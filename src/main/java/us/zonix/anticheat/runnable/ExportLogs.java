package us.zonix.anticheat.runnable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.Log;
import us.zonix.anticheat.requests.MemeFetchRequest;
import us.zonix.core.CorePlugin;

import java.sql.Timestamp;
import java.util.*;

public class ExportLogs implements Runnable
{
    private Queue<Log> logs;
    private LordMeme plugin;
    private int count;
    
    public ExportLogs(LordMeme plugin) {
        this.plugin = plugin;
        this.logs = plugin.getLogManager().getLogQueue();
        this.count = 0;
    }

    @Override
    public void run() {

        if (this.logs.isEmpty()) {
            return;
        }

        final double start = System.currentTimeMillis();
        final Set<JsonArray> data = new HashSet<JsonArray>();
        JsonArray current = new JsonArray();
        for (final Log log : this.logs) {
            final JsonObject object = new JsonObject();
            object.addProperty("time", new Timestamp(log.getTimestamp()).toString());
            object.addProperty("uuid", log.getUUID().toString());
            object.addProperty("message", log.getLog());
            current.add(object.toString());
            if (current.toString().length() >= 1000) {
                data.add(current);
                current = new JsonArray();
            }
            this.plugin.getLogManager().removeFromLogQueue(log);
        }
        if (current.size() > 0) {
            data.add(current);
        }
        final int max = data.size();
        for (final JsonArray array : data) {
            CorePlugin.getInstance().getRequestProcessor().sendRequestAsync(new MemeFetchRequest.InsertRequest(array), element -> {
                final JsonObject jsonData = element.getAsJsonObject();
                final String response = jsonData.get("response").getAsString();
                if (!response.equals("success")) {
                    this.onError(jsonData.getAsString());
                }
                if (++this.count >= max) {
                    final double end = System.currentTimeMillis();
                    this.plugin.getLogger().info(String.format("%sThe logs were exported in %.2fs", ChatColor.GREEN, (end - start) / 1000.0));
                }
            });
        }
    }

    private void onError(final String message) {
        this.plugin.getLogger().warning(ChatColor.RED + "ERROR while exporting Lord_MeMe logs.");
        this.plugin.getLogger().severe(message);
    }
}
