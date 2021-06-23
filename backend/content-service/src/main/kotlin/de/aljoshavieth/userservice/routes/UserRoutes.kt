import de.aljoshavieth.userservice.manager.MongoManager
import de.aljoshavieth.userservice.models.User
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}

fun Route.userRouting() {
    val mongoManager = MongoManager.getInstance();
    route("/user") {
        get {
            val users = mongoManager.usersFromMongoDB;
            if (users.size < 1) {
                call.respond("{\"status\": failure, \"message\": \"No users found!\"}");
            } else {
                call.respond(users)
            }
        }
        get("{id}") {
            val id = call.parameters["id"]
                ?: return@get call.respond("{\"status\": failure, \"message\": \"Missing or malformed id!\"}")
            val user = mongoManager.getUserFromMongoDB(id);
            if (user == null) {
                call.respond("{\"status\": failure, \"message\": \"User not found!\"}");
            } else {
                call.respond(user)
            }

        }
        post {
            try {
                val user = call.receive<User>()
                val manager = MongoManager.getInstance();
                manager.insertUserToMongoDB(user);
                call.respond("{\"status\": success, \"message\": \"User stored correctly\"}");
            } catch (e: Exception) {
                call.respond("{\"status\": failure, \"message\": \"User format is not valid, check your request body!\"}");
            }
        }
        delete("{id}") {
            val manager = MongoManager.getInstance();
            if (manager.removeUserFromMongoDB(call.parameters["id"])) {
                call.respond("{\"status\": success, \"message\": \"User removed correctly\"}");
            } else {
                call.respond("{\"status\": failure, \"message\": \"user not found, hence cannot be deleted!\"}");
            }
        }
    }
}
