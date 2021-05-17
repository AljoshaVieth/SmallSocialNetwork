package de.aljoshavieth.userservice.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val firstName: String, val lastName: String, val email: String)

val userStorage = mutableListOf<User>()

