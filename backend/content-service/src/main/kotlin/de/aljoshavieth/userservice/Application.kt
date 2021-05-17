package de.aljoshavieth.userservice

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import registerUserRoutes
import kotlin.system.exitProcess


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    // allow access to frontend via cors https://ktor.io/docs/cors.html#configuration
    install(CORS)
    {
        //anyHost()
        host("localhost:63342")
    }
    registerUserRoutes()
}
