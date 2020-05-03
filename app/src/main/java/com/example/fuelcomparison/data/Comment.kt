package com.example.fuelcomparison.data

import java.io.Serializable

class Comment : Serializable {
    var id: Long = 0
    var user_id: Long = 0
    var rating: Long = 0
    var content: String? = null
    override fun toString(): String {
        return "Comment{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", rating=" + rating +
                ", content='" + content + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = 7747284152827699862L
    }
}