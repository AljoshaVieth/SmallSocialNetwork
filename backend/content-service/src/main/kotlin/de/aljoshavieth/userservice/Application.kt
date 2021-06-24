package de.aljoshavieth.userservice

import de.aljoshavieth.userservice.routes.registerPostRoutes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.serialization.*
import registerUserRoutes
import org.slf4j.event.Level


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging) {
        level = Level.INFO
        format { call -> call.request.contentType().toString() }
    }
    // allow access to frontend via cors https://ktor.io/docs/cors.html#configuration
    install(CORS)
    {
        anyHost()
        //host("localhost:63342")
    }
    registerUserRoutes()
    registerPostRoutes()
}
