package us.zonix.anticheat.util.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import us.zonix.anticheat.LordMeme;

import java.util.Arrays;

public class CheatDatabase {

    @Getter private final MongoClient client;
    @Getter private final MongoDatabase database;
    @Getter private final MongoCollection logs;

    public CheatDatabase(LordMeme main) {

        if (main.getConfigFile().getBoolean("DATABASE.MONGO.AUTHENTICATION.ENABLED")) {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")), Arrays.asList(MongoCredential.createCredential(main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.USER"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.DATABASE"), main.getConfigFile().getString("DATABASE.MONGO.AUTHENTICATION.PASSWORD").toCharArray())));
        } else {
            client = new MongoClient(new ServerAddress(main.getConfigFile().getString("DATABASE.MONGO.HOST"), main.getConfigFile().getInt("DATABASE.MONGO.PORT")));
        }

        database = client.getDatabase("anticheat");
        logs = database.getCollection("logs");
    }

}
