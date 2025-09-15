package com.example.feature.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.message.usecase.GetMessagesUseCase
import com.example.feature.message.state.MessageViewState
import com.example.feature.message.state.State
import com.example.feature.message.state.pushToSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.apply
import kotlin.collections.toMutableList

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getMessageUseCase: GetMessagesUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<State<MessageViewState>> = MutableStateFlow(
        State.Loading(MessageViewState())
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            // added delay to simulate screen loading (first loading)
            delay(timeMillis = 1000L)
            // get first page of messages
            getMessage(loadMore = false)
        }
    }

    fun getMessage(loadMore: Boolean) {
        if (!getItemsState().isEmpty() && !loadMore) return
        if (getIsLoadingMoreState()) return
        // i know that create a scope here is not best practice but it is for simulate loading
        viewModelScope.launch {
            // loading for every new pagination request
            if (loadMore) {
                _state.update {
                    it.data?.copy(isLoadingMore = true).pushToSuccess()
                }
                // added delay to simulate loading time
                delay(timeMillis = 2000L)
            }
            // success
            val messages = getMessageUseCase()
            val newItems = getItemsState().toMutableList().apply {
                if (!loadMore) clear()
                addAll(messages)
            }.toImmutableList()
            _state.update {
                it.data?.copy(items = newItems, isLoadingMore = false).pushToSuccess()
            }
        }
    }

    private fun getItemsState() = state.value.data?.items ?: persistentListOf()
    private fun getIsLoadingMoreState() = state.value.data?.isLoadingMore ?: false
}