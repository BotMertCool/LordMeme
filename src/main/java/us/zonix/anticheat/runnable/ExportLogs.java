package us.zonix.anticheat.runnable;

import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.Log;

import java.util.regex.*;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

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

        Document document = new Document();

        final Pattern pattern = Pattern.compile("\\s");
        for (final Log log : this.logs) {

            document.put("uuid", log.getUUID());
            document.put("time", log.getTimestamp());
            document.put("message", log.getLog());

            plugin.getCheatDatabase().getLogs().replaceOne(eq("uuid", log.getUUID()), document, new UpdateOptions().upsert(true));

            this.plugin.getLogManager().removeFromLogQueue(log);
        }

        System.out.println("[LordMeMe] Sent logs to database: " + this.logs.size());
    }

}
