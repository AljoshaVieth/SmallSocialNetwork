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
                call.respond("{\"status\": failure, \"message\": \"No post found!\"}");
            } else {
                call.respond(posts)
            }
        }
        get("{id}") {
            val id = call.parameters["id"]
                ?: return@get call.respond("{\"status\": failure, \"message\": \"Missing or malformed id!\"}");
            val post = mongoManager.getPostFromMongoDB(id);
            if (post == null) {
                call.respond("{\"status\": failure, \"message\": \"Post not found!\"}");
            } else {
                call.respond(post)
            }

        }
        post {
            try {
                val post = call.receive<Post>()
                val manager = MongoManager.getInstance();
                manager.insertPostToMongoDB(post);
                manager.insertUserToMongoDB(post.author);
                call.respond("{\"status\": success, \"message\": \"Post stored correctly\"}");
            } catch (e: Exception) {
                call.respond("{\"status\": failure, \"message\": \"Post format is not valid, check your request body!\"}");
            }
        }
        delete("{id}") {
            val manager = MongoManager.getInstance();
            if (manager.removePostFromMongoDB(call.parameters["id"])) {
                call.respond("{\"status\": success, \"message\": \"Post removed correctly\"}");
            } else {
                call.respond("{\"status\": failure, \"message\": \"Post not found, hence cannot be deleted!\"}");
            }
        }
    }
}
