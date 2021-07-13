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
                .append("authorId", post.getAuthor().getId())
                .append("time", post.getTime());
    }

    private static List<DBObject> getDBObjectFromArray(Comment[] comments) {
        List<DBObject> array = new ArrayList<DBObject>();
        for (Comment comment : comments) {
            array.add(new BasicDBObject("_id", comment.getId()).append("content", comment.getContent()).append("authorId",
                    comment.getAuthor().getId()).append("time", comment.getTime()));
        }
        return array;
    }

    public static Post fromDBObject(DBObject postDBObject){
        String authorId = (String) postDBObject.get("authorId");
        User author = new User(authorId, "Anonymous");
        BasicDBList commentsDBList = (BasicDBList) postDBObject.get("comments");
        ArrayList<Comment> comments = new ArrayList<>();
        commentsDBList.forEach(c -> comments.add(getCommentFromObject(c)));
        Comment[] commentArray = new Comment[comments.size()];
        commentArray = comments.toArray(commentArray);
        return new Post((String) postDBObject.get("_id"), (String) postDBObject.get("content"), (String) postDBObject.get("color"), commentArray, author, (long) postDBObject.get("time"));
    }

    private static Comment getCommentFromObject(Object o){
        BasicDBObject commentDBBObject = (BasicDBObject) o;
        String id = (String) commentDBBObject.get("_id");
        String content = (String) commentDBBObject.get("content");
        String authorId = (String) commentDBBObject.get("authorId");
        long time  = (long) commentDBBObject.get("time");
        User author = new User(authorId, "Anonymous");
        return new Comment(id, content, author, time);

    }
}