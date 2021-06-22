package de.aljoshavieth.userservice.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: String, val name: String) : ContentServiceModel

