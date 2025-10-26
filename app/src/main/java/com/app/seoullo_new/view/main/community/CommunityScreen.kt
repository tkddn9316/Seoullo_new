package com.app.seoullo_new.view.main.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.domain.model.common.ApiState
import com.app.seoullo_new.view.base.ErrorScreen
import com.app.seoullo_new.view.base.LoadingOverlay
import com.app.seoullo_new.view.util.navigation.Route

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = hiltViewModel(),
    communityOnClick: (destination: String, postId: String) -> Unit
) {
    val postListState by viewModel.posts.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { communityOnClick(Route.ADD_POST, "") }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
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
                            PostList(item = post) {
                                communityOnClick(Route.DETAIL_POST, post.id)
                            }
                        }
                    }
                }
            }
            else -> Unit
        }
    }
}