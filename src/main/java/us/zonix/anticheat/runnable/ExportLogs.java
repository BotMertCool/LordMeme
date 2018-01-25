package us.zonix.anticheat.runnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.Log;
import us.zonix.anticheat.requests.MemeFetchRequest;
import us.zonix.core.CorePlugin;
import us.zonix.core.api.callback.AbstractBukkitCallback;
import us.zonix.core.api.request.PunishmentRequest;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Queue;

public class ExportLogs implements Runnable
{
    private Queue<Log> logs;
    private LordMeme plugin;
    private int count;
    
    public ExportLogs(LordMeme plugin) {
        this.plugin = plugin;
        this.count = 0;
        this.logs = plugin.getLogManager().getLogQueue();
    }

    @Override
    public void run() {

        if (this.logs.isEmpty()) {
            return;
        }

        this.count = 0;

        Iterator<Log> logIterator = this.logs.iterator();

        while (logIterator.hasNext()) {
            Log log = logIterator.next();

            JsonObject object = new JsonObject();
            object.addProperty("uuid", log.getUUID().toString());
            object.addProperty("time", new Timestamp(log.getTimestamp()).toString());
            object.addProperty("message", log.getLog());

            CorePlugin.getInstance().getRequestProcessor().sendRequestAsync(new MemeFetchRequest.InsertRequest(object), new AbstractBukkitCallback() {
                @Override
                public void callback(JsonElement response) {
                    if (response == null || response.isJsonNull() || response.isJsonPrimitive()) {
                        System.out.println(ChatColor.RED + "Failed to contact the database...");
                        return;
                    }

                    count++;

                }

                @Override
                public void onError(String message) {
                    this.callback(null);
                }
            });

            logIterator.remove();
        }

        plugin.getLogger().info(this.count + " logs have been exported.");
    }
}
