package de.aljoshavieth.userservice.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(val id: String, val content: String,val color:String, val comments: Array<Comment>, val author: User, val time: Long) : ContentServiceModel{
    // auto generated...
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (content != other.content) return false
        if (content != other.color) return false
        if (!comments.contentEquals(other.comments)) return false
        if (author != other.author) return false
        if(time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + comments.contentHashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + time.hashCode()
        return result
    }


}
