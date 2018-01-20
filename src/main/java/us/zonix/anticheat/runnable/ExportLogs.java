package us.zonix.anticheat.runnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.Log;
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
    
    public ExportLogs(LordMeme plugin) {
        this.plugin = plugin;
        this.logs = plugin.getLogManager().getLogQueue();
    }

    @Override
    public void run() {

        if (this.logs.isEmpty()) {
            return;
        }

        Iterator<Log> logIterator = this.logs.iterator();

        while (logIterator.hasNext()) {
            Log log = logIterator.next();

            JsonObject object = new JsonObject();
            object.addProperty("uuid", log.getUUID().toString());
            object.addProperty("time", new Timestamp(log.getTimestamp()).toString());
            object.addProperty("message", log.getLog());

            CorePlugin.getInstance().getRequestProcessor().sendRequestAsync(new PunishmentRequest.InsertRequest(object), new AbstractBukkitCallback() {
                @Override
                public void callback(JsonElement response) {
                    if (response == null || response.isJsonNull() || response.isJsonPrimitive()) {
                        System.out.println(ChatColor.RED + "Failed to contact the database...");
                        return;
                    }

                    plugin.getLogger().info("The logs have been exported.");
                }
            });

            logIterator.remove();
        }
    }
}
