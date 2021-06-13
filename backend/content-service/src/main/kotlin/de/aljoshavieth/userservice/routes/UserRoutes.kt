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
                call.respondText("No users found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(users)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val user = mongoManager.getUserFromMongoDB(id);
            if (user == null) {
                call.respondText("User not found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(user)
            }

        }
        post {
            try {
                val user = call.receive<User>()
                val manager = MongoManager.getInstance();
                manager.insertUserToMongoDB(user);
                call.respondText("User stored correctly", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText(
                    "User format is not valid, check your request body!",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        delete("{id}") {
            val manager = MongoManager.getInstance();
            if (manager.removeUserFromMongoDB(call.parameters["id"])) {
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("User not found, hence cannot be deleted!", status = HttpStatusCode.NotFound)
            }
        }
    }
}
