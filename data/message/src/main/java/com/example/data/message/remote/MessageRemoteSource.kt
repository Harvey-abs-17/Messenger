package com.example.data.message.remote

import android.os.SystemClock
import com.example.data.message.model.MessageDTO
import java.util.UUID

class MessageRemoteSource {
    fun getMessages(): List<MessageDTO> {
        val messages = mutableListOf<MessageDTO>()
        for (i in 1..50) {
            messages.add(
                MessageDTO(
                    id = UUID.randomUUID().toString(),
                    text = "hello $i",
                    date = SystemClock.uptimeMillis(),
                    sendByMe = i % 2 == 0
                )
            )
        }
        return messages
    }
}