package com.example.domain.message.usecase

import com.example.data.message.repository.MessageRepository
import com.example.domain.message.model.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    val messageRepository: MessageRepository
) {
    operator fun invoke(): ImmutableList<Message> {
        return messageRepository.getMessages().map { messageDTO ->
            Message(
                id = messageDTO.id,
                text = messageDTO.text,
                date = messageDTO.date,
                sendByMe = messageDTO.sendByMe
            )
        }.toImmutableList()
    }
}