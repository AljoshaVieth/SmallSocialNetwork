package de.aljoshavieth.userservice.manager;

import com.mongodb.BasicDBList;
import de.aljoshavieth.userservice.models.Comment;
import de.aljoshavieth.userservice.models.Post;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import de.aljoshavieth.userservice.models.User;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter {
    public static DBObject toDBObject(Post post) {
        getDBObjectFromArray(post.getComments());
        return new BasicDBObject("_id", post.getId())
                .append("content", post.getContent())
                .append("color", post.getColor())
                .append("comments", getDBObjectFromArray(post.getComments()))
                .append("author", new BasicDBObject("_id", post.getAuthor().getId()).append("name", post.getAuthor().getName()))
                .append("time", post.getTime());
    }

    private static List<DBObject> getDBObjectFromArray(Comment[] comments) {
        List<DBObject> array = new ArrayList<DBObject>();
        for (Comment comment : comments) {
            array.add(new BasicDBObject("_id", comment.getId()).append("content", comment.getContent()).append("user",
                    new BasicDBObject("_id", comment.getUser().getId()).append("name", comment.getUser().getName())));
        }
        return array;
    }

    public static Post fromDBObject(DBObject postDBObject){
        String authorId = (String) ((DBObject)postDBObject.get("author")).get("_id");
        String authorName = (String) ((DBObject)postDBObject.get("author")).get("name");
        User author = new User(authorId, authorName);
        BasicDBList commentsDBList = (BasicDBList) postDBObject.get("comments");
        ArrayList<Comment> comments = new ArrayList<>();
        commentsDBList.forEach(c -> comments.add((Comment) c));
        Comment[] commentArray = new Comment[comments.size()];
        commentArray = comments.toArray(commentArray);
        return new Post((String) postDBObject.get("_id"), (String) postDBObject.get("content"), (String) postDBObject.get("color"), commentArray, author, (long) postDBObject.get("time"));
    }
}