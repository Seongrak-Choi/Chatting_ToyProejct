package com.example.chatting_toyproject.model

class CC {
    var users: Map<String, Boolean> = HashMap()
    var comments: Map<String, Comment> = HashMap()

    class Comment {
        var uid: String? = null
        var message: String? = null
        var timeStamp: Any? = null
    }
}