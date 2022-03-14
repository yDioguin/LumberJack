package pt.dioguin.lumberjack.utils;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bukkit.Bukkit;
import pt.dioguin.lumberjack.Main;
import pt.dioguin.lumberjack.objects.LumberJack;

import java.util.UUID;

public class MongoDB {

    private static MongoCollection<Document> collection;

    public static void openConnection(){

        String uri = "mongodb://diogo:benficaLixo123@cluster0-shard-00-00.3qccm.mongodb.net:27017,cluster0-shard-00-01.3qccm.mongodb.net:27017,cluster0-shard-00-02.3qccm.mongodb.net:27017/test?ssl=true&replicaSet=atlas-o9l5wh-shard-0&authSource=admin&retryWrites=true&w=majority";
        MongoClientURI clientURI = new MongoClientURI(uri);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("Test");
        collection = mongoDatabase.getCollection("lumberjack");

        Bukkit.getConsoleSender().sendMessage("§aConnection open!");

    }

    public static void loadPlayer(String uuid){

        Document document = new Document("UUID", uuid);
        Document found = (Document) collection.find(document).first();

        if (found == null){

            document.append("levelMax", 0);
            document.append("level", 0);
            document.append("xp", 0);
            document.append("xpNecessary", 0);
            collection.insertOne(document);

            LumberJack lumberjack = new LumberJack(Main.getInstance().getConfig().getInt("LumberJack.levelMax"), 0, 0, Main.getInstance().getConfig().getDouble("LumberJack.xpMultiplierInitial"), 0);
            LumberJack.playersData.put(UUID.fromString(uuid), lumberjack);

        }else{

            int levelMax = found.getInteger("levelMax");
            int level = found.getInteger("level");
            double xp = found.getDouble("xp");
            double xpNecessary = found.getDouble("xpNecessary");

            LumberJack lumberjack = new LumberJack(levelMax, level, xp, xpNecessary, 0);
            LumberJack.playersData.put(UUID.fromString(uuid), lumberjack);

        }

    }

    public static void savePlayer(String uuid, int levelMax, int level, double xp, double xpNecessary){

        Document document = new Document("UUID", uuid);
        Document found = (Document) collection.find(document).first();

        if (found != null){
            Document replaceDocument = new Document("UUID", uuid);
            replaceDocument.append("levelMax", levelMax);
            replaceDocument.append("level", level);
            replaceDocument.append("xp", xp);
            replaceDocument.append("xpNecessary", xpNecessary);
            collection.replaceOne(document, replaceDocument);
        }

    }

}
