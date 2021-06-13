package de.aljoshavieth.userservice.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(val id: String, val content: String, val user: User)