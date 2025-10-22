package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    communityOnClick: (String) -> Unit
) {
    val postListState by viewModel.posts.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0)   //  안드 15 불필요한 위/아래 패딩 제거
    ) { innerPadding ->
        when (val s = postListState) {
            is ApiState.Loading -> LoadingOverlay()
            is ApiState.Error -> ErrorScreen(reason = s.message.orEmpty())
            is ApiState.Success -> {
                val postList = s.data.orEmpty()
                if (postList.isEmpty()) {
//                    EmptyScreen(text = "게시글이 없습니다.")
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = rememberLazyListState()
                    ) {
                        items(
                            items = postList,
                            key = { it.id }
                        ) { post ->
                            PostItem(item = post) {
                                communityOnClick(post.id)
                            }
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}