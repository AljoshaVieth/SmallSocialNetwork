package de.aljoshavieth.userservice.manager;

import de.aljoshavieth.userservice.models.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserAdapter {
    public static DBObject toDBObject(User user) {
        return new BasicDBObject("_id", user.getId())
                .append("name", user.getName());
    }
}
