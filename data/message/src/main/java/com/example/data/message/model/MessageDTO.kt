package com.example.data.message.model

data class MessageDTO(
    val id: String,
    val text: String,
    val date: Long,
    val sendByMe: Boolean
)
