package de.aljoshavieth.userservice.manager;

import de.aljoshavieth.userservice.models.Post;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class PostAdapter {
    public static DBObject toDBObject(Post post) {
        return new BasicDBObject("_id", post.getId())
                .append("content", post.getContent())
                .append("comments", post.getComments())
                .append("user", post.getAuthor());
    }
}