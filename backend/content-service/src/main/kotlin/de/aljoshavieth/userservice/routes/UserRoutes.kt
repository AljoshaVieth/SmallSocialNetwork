import de.aljoshavieth.userservice.models.User
import de.aljoshavieth.userservice.models.userStorage
import de.aljoshavieth.userservice.manager.MongoManager
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
            // TODO Check if any users exist
            //call.respondText("No users found", status = HttpStatusCode.NotFound)
            val user = mongoManager.getUserFromMongoDB();
            call.respond(user)
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            // TODO Check if user exists
            //call.respondText("User not found", status = HttpStatusCode.NotFound)

            val user = mongoManager.getUserFromMongoDB(id);
            call.respond(user)
        }
        post {
            val user = call.receive<User>()
            val mongoManager = MongoManager.getInstance();
            mongoManager.insertInMongoDB(user);
            userStorage.add(user)
            call.respondText("User stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            //TODO migrate from storage to mongodb
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (userStorage.removeIf { it.id == id }) {
                call.respondText("User removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}
