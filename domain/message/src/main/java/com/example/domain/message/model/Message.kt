package com.example.domain.message.model

data class Message(
    val id: String,
    val text: String,
    val date: Long,
    val sendByMe: Boolean
)
