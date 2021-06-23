package de.aljoshavieth.userservice.manager;

import de.aljoshavieth.userservice.models.Comment;
import de.aljoshavieth.userservice.models.Post;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter {
    public static DBObject toDBObject(Post post) {
        getDBObjectFromArray(post.getComments());
        return new BasicDBObject("_id", post.getId())
                .append("content", post.getContent())
                .append("color", post.getColor())
                .append("comments", getDBObjectFromArray(post.getComments()))
                .append("user", new BasicDBObject("_id", post.getAuthor().getId()).append("name", post.getAuthor().getName()));
    }

    private static List<DBObject> getDBObjectFromArray(Comment[] comments) {
        List<DBObject> array = new ArrayList<DBObject>();
        for (Comment comment : comments) {
            array.add(new BasicDBObject("_id", comment.getId()).append("content", comment.getContent()).append("user",
                    new BasicDBObject("_id", comment.getUser().getId()).append("name", comment.getUser().getName())));
        }
        return array;
    }
}