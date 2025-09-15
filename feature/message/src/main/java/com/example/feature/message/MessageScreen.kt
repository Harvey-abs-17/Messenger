package com.example.feature.message

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.message.model.Message
import com.example.feature.message.state.Draw
import kotlinx.collections.immutable.ImmutableList
import kotlin.collections.isNotEmpty
import kotlin.collections.lastIndex
import kotlin.collections.lastOrNull

@Composable
fun MessageScreen(
    modifier: Modifier = Modifier,
    viewModel: MessageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    state.Draw(
        onLoading = {
            MessageLoading(modifier)
        },
        onSuccess = { viewState ->
            MessageScreenContent(
                items = viewState.items,
                isLoadingMore = viewState.isLoadingMore,
                isLastPage = viewState.isLastPage,
                loadMore = { viewModel.getMessage(loadMore = true) }
            )
        },
        onError = {}
    )
}

@Composable
private fun MessageLoading(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessageScreenContent(
    items: ImmutableList<Message>,
    loadMore: () -> Unit,
    isLoadingMore: Boolean,
    isLastPage: Boolean
) {
    PaginationLazyColumn(
        items = items,
        loadMore = loadMore,
        isLoadingMore = isLoadingMore,
        isLastPage = isLastPage
    ) {
        items(count = items.size, key = { index -> items[index].id }) { index ->
            val item = items[index]
            MessageItem(message = item.text, sendByMe = item.sendByMe)
        }
    }
}

@Composable
private fun MessageItem(message: String, sendByMe: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (sendByMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (sendByMe) Color(0xFFDCF8C6) else Color(0xFF3F51B5),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun PaginationLazyColumn(
    items: ImmutableList<Any>,
    loadMore: () -> Unit,
    isLoadingMore: Boolean,
    isLastPage: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(space = 8.dp),
    verticalPaddingSize: Dp = 0.dp,
    horizontalPaddingSize: Dp = 16.dp,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.(item: Any) -> Unit
) {
    val visibleItemsInfo by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo }
    }
    val lastVisibleItemIndex by remember {
        derivedStateOf { visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }

    LaunchedEffect(lastVisibleItemIndex >= items.lastIndex) {
        snapshotFlow {
            lastVisibleItemIndex >= items.lastIndex &&
                    !isLastPage &&
                    !isLoadingMore &&
                    items.isNotEmpty()
        }.collect { isLastItem ->
            if (isLastItem) {
                loadMore()
            }
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        modifier = modifier.padding(
            horizontal = horizontalPaddingSize,
            vertical = verticalPaddingSize
        )
    ) {
        content(items)
        item {
            LoadingPagination(
                modifier = Modifier.navigationBarsPadding(),
                isVisible = isLoadingMore
            )
        }
    }
}

@Composable
private fun LoadingPagination(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier.fillMaxWidth(),
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = Color.Blue
                )
            }
        }
    )
}