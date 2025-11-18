package com.app.seoullo_new.view.main.community.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.app.seoullo_new.utils.PostOption
import com.app.seoullo_new.view.ui.theme.seoulloLightGray

/**
 * 글쓰기 옵션(사진 등)
 */
@Composable
fun PostOptionRow(
    modifier: Modifier = Modifier,
    onOptionClick: (PostOption) -> Unit
) {
    val options = remember {
        listOf(
            PostOption("photo")
        )
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(
            items = options,
            key = { option -> option.id }
        ) { option ->
            IconButton(
                modifier = modifier.size(40.dp),
                onClick = { onOptionClick(option) }
            ) {
                Icon(
                    imageVector = idToIcon(option.id),
                    contentDescription = null,
                    tint = seoulloLightGray
                )
            }
        }
    }
}

@Composable
private fun idToIcon(id: String): ImageVector = when (id) {
    "photo" -> Icons.Outlined.Image
    else -> Icons.Outlined.Image
}