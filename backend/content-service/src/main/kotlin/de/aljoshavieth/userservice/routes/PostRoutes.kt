package de.aljoshavieth.userservice.routes

import de.aljoshavieth.userservice.manager.MongoManager
import de.aljoshavieth.userservice.models.Post
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.registerPostRoutes() {
    routing {
        postRouting()
    }
}

fun Route.postRouting() {
    val mongoManager = MongoManager.getInstance();
    route("/post") {
        get {
            val posts = mongoManager.postsFromMongoDB;
            if (posts.size < 1) {
                call.respondText("No posts found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(posts)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val post = mongoManager.getPostFromMongoDB(id);
            if (post == null) {
                call.respondText("Post not found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(post)
            }

        }
        post {
            try {
                val post = call.receive<Post>()
                val manager = MongoManager.getInstance();
                manager.insertPostToMongoDB(post);
                call.respondText("Post stored correctly", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText(
                    "Post format is not valid, check your request body!",
                    status = HttpStatusCode.BadRequest
                )
            }
        }
        delete("{id}") {
            val manager = MongoManager.getInstance();
            if (manager.removePostFromMongoDB(call.parameters["id"])) {
                call.respondText("Post removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Post not found, hence cannot be deleted!", status = HttpStatusCode.NotFound)
            }
        }
    }
}
