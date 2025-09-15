package com.example.data.message.repository

import com.example.data.message.model.MessageDTO

interface MessageRepository {
    fun getMessages(): List<MessageDTO>
}