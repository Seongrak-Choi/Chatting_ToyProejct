package com.example.chatting_toyproject.model

data class ChatModel(
    var users: HashMap<String, Boolean> = HashMap(), //채팅방의 유저들
    var comments : Map<String,Comment> = HashMap() //채팅방의 내용
)

data class Comment(
    var uid: String ="",
    var message: String ="",
    var timeStamp : Any ?= null
)
