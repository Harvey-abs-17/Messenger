package com.example.data.message.repository

import com.example.data.message.model.MessageDTO
import com.example.data.message.remote.MessageRemoteSource
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    val messageRemoteSource: MessageRemoteSource
) : MessageRepository {
    override fun getMessages(): List<MessageDTO> {
        return messageRemoteSource.getMessages()
    }
}