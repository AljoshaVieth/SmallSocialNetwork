package de.aljoshavieth.userservice.manager;

import com.mongodb.*;
import de.aljoshavieth.userservice.models.User;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoManager {
    private static MongoManager instance;
    private MongoClient mongoClient;

    private MongoManager() {
    }

    private DB database;
    private DBCollection collection;
    private Properties properties;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    public static MongoManager getInstance() {
        if (MongoManager.instance == null) {
            MongoManager.instance = new MongoManager();
            MongoManager.instance.init();
        }
        return MongoManager.instance;
    }

    private void init() {
        logger.setLevel(Level.INFO);
        if (loadProperties()) {
            logger.info("Properties loaded successfully");
        } else {
            logger.log(Level.SEVERE, "Could not load properties... please check your properties file and restart!");
            return;
        }
        connectToMongoDB();

    }

    boolean loadProperties() {
        boolean success = false;
        properties = new Properties();
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(new FileInputStream("config.properties"));
            properties.load(stream);
            stream.close();
            success = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while loading properties file!");
            logger.log(Level.SEVERE, e.getMessage());
        }
        return success;
    }

    private void connectToMongoDB() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(String.valueOf(properties.get("db.host"))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        database = mongoClient.getDB(String.valueOf(properties.get("db.databasename")));
        try {
            database.command("ping");
            collection = database.getCollection(String.valueOf(properties.get("db.collectionname")));
            logger.info("Successfully connected to MongoDB at " + properties.get("db.host"));
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Could not connect to mongodb... please check your properties file and restart!");
        }
    }

    public void insertInMongoDB(User user) {
        collection.insert(UserAdapter.toDBObject(user));
        logger.info("Inserted user " + user.getEmail() + " to " + properties.get("db.collectionname"));
    }

    public User getUserFromMongoDB(String id) {
        DBObject query = new BasicDBObject("_id", id);
        DBCursor cursor = collection.find(query);
        DBObject user = cursor.one();
        return new User(id, (String) user.get("firstName"), (String) user.get("lastName"), (String) user.get("email"));
    }

    public ArrayList<User> getUserFromMongoDB() {
        ArrayList<User> users = new ArrayList<>();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject userDBObject = cursor.next();
            User user = new User((String) userDBObject.get("_id"), (String) userDBObject.get("firstName"), (String) userDBObject.get("lastName"), (String) userDBObject.get("email"));
            users.add(user);
        }
        return users;
    }


    public void closeConnectionToMongoDB() {
        mongoClient.close();
        logger.info("Successfully disconnected from MongoDB at " + properties.get("db.host"));
    }


}
