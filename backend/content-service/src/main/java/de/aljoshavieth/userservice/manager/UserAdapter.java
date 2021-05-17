package de.aljoshavieth.userservice.manager;

import de.aljoshavieth.userservice.models.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UserAdapter {
    public static final DBObject toDBObject(User customer) {
        return new BasicDBObject("_id", customer.getId())
                .append("firstName", customer.getFirstName())
                .append("lastName", customer.getLastName())
                .append("email", customer.getEmail());
    }
}
