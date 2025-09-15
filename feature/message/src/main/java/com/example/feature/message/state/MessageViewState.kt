package com.example.feature.message.state

import com.example.domain.message.model.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MessageViewState(
    val items: ImmutableList<Message> = persistentListOf(),
    val isLoadingMore : Boolean = false,
    val isLastPage : Boolean = false
)