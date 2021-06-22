package de.aljoshavieth.userservice.manager;

import com.mongodb.*;
import de.aljoshavieth.userservice.models.*;

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

    private String userCollectionName;
    private String postCollectionName;
    private DBCollection userCollection;
    private DBCollection postCollection;
    private Properties properties;
    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


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
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream("config.properties"));
            properties.load(stream);
            stream.close();
            success = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while loading properties file!");
            logger.log(Level.SEVERE, e.getMessage());
        }
        userCollectionName = String.valueOf(properties.get("db.usercollectionname"));
        postCollectionName = String.valueOf(properties.get("db.postcollectionname"));
        return success;
    }

    private void connectToMongoDB() {
        try {
            mongoClient = new MongoClient(new MongoClientURI(String.valueOf(properties.get("db.host"))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DB database = mongoClient.getDB(String.valueOf(properties.get("db.databasename")));
        try {
            database.command("ping");
            userCollection = database.getCollection(userCollectionName);
            postCollection = database.getCollection(postCollectionName);
            logger.info("Successfully connected to MongoDB at " + properties.get("db.host"));
        } catch (MongoException e) {
            logger.log(Level.SEVERE, "Could not connect to mongodb... please check your properties file and restart!");
        }
    }

    // For better method naming...
    public void insertUserToMongoDB(User user) {
        insertToMongoDB(user);
    }

    public void insertPostToMongoDB(Post post) {
        insertToMongoDB(post);
    }

    private void insertToMongoDB(ContentServiceModel object) {
        if (object instanceof User) {
            User user = (User) object;
            userCollection.insert(UserAdapter.toDBObject(user));
            logger.info("Inserted user " + user.getId() + " to " + userCollectionName);
        } else {
            Post post = (Post) object;
            postCollection.insert(PostAdapter.toDBObject(post));
            logger.info("Inserted post from " + post.getAuthor().getId() + " with id= " + post.getId() + " into " + postCollectionName);
        }
    }

    public boolean removeUserFromMongoDB(String id) {
        return removeFromMongoDB(ContentServiceModelType.USER, id);
    }

    public boolean removePostFromMongoDB(String id) {
        return removeFromMongoDB(ContentServiceModelType.POST, id);
    }

    private boolean removeFromMongoDB(ContentServiceModelType type, String id) {
        WriteResult writeResult;
        if (type == ContentServiceModelType.USER) {
            writeResult = userCollection.remove(new BasicDBObject("_id", id));
        } else {
            writeResult = postCollection.remove(new BasicDBObject("_id", id));
        }
        return writeResult.getN() == 1;
    }

    public User getUserFromMongoDB(String id) {
        DBObject query = new BasicDBObject("_id", id);
        DBCursor cursor = userCollection.find(query);
        DBObject user = cursor.one();
        return user == null ? null : new User(id, (String) user.get("name"));
    }

    public Post getPostFromMongoDB(String id) {
        DBObject query = new BasicDBObject("_id", id);
        DBCursor cursor = postCollection.find(query);
        DBObject post = cursor.one();
        return post == null ? null : new Post(id, (String) post.get("content"), (String) post.get("color"), (Comment[]) post.get("comments"), (User) post.get("author"));
    }

    public ArrayList<User> getUsersFromMongoDB() {
        ArrayList<User> users = new ArrayList<>();
        DBCursor cursor = userCollection.find();
        while (cursor.hasNext()) {
            DBObject userDBObject = cursor.next();
            User user = new User((String) userDBObject.get("_id"), (String) userDBObject.get("name"));
            users.add(user);
        }
        return users;
    }

    public ArrayList<Post> getPostsFromMongoDB() {
        ArrayList<Post> posts = new ArrayList<>();
        DBCursor cursor = postCollection.find();
        while (cursor.hasNext()) {
            DBObject postDBObject = cursor.next();
            Post post = new Post((String) postDBObject.get("_id"), (String) postDBObject.get("content"), (String) postDBObject.get("color"),(Comment[]) postDBObject.get("comments"), (User) postDBObject.get("author"));
            posts.add(post);
        }
        return posts;
    }


    public void closeConnectionToMongoDB() {
        mongoClient.close();
        logger.info("Successfully disconnected from MongoDB at " + properties.get("db.host"));
    }


}
