package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.seoullo_new.R

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    enabled: Boolean = true,
    onItemClick: () -> Unit,
    showTrailingIcon: Boolean,      // 오른쪽 아이콘
    showLeadingIcon: Boolean,       // 왼쪽 아이콘
    leadingIcon: @Composable () -> Unit? = {}
) {
    val clickableModifier = if (enabled) {
        modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(horizontal = 8.dp)
    } else {
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    }
    val colors = ListItemDefaults.colors()

    if (showLeadingIcon) {
        ListItem(
            modifier = clickableModifier,
            headlineContent = { Text(title, overflow = TextOverflow.Ellipsis) },
            supportingContent = {
                description?.let { Text(it, overflow = TextOverflow.Ellipsis) }
            },
            leadingContent = { leadingIcon() },
            trailingContent = {
                if (showTrailingIcon) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_arrow_right),
                        contentDescription = null
                    )
                }
            },
            colors = ListItemDefaults.colors(
                headlineColor = if (enabled) colors.headlineColor else colors.disabledHeadlineColor,
                supportingColor = if (enabled) colors.supportingTextColor else colors.disabledHeadlineColor,
                trailingIconColor = if (enabled) colors.trailingIconColor else colors.disabledTrailingIconColor
//                containerColor = MaterialTheme.colorScheme.background
            )
        )
    } else {
        ListItem(
            modifier = clickableModifier,
            headlineContent = { Text(title) },
            supportingContent = {
                description?.let { Text(it) }
            },
            trailingContent = {
                if (showTrailingIcon) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_setting_arrow_right),
                        contentDescription = null
                    )
                }
            },
            colors = ListItemDefaults.colors(
                headlineColor = if (enabled) colors.headlineColor else colors.disabledHeadlineColor,
                supportingColor = if (enabled) colors.supportingTextColor else colors.disabledHeadlineColor,
                trailingIconColor = if (enabled) colors.trailingIconColor else colors.disabledTrailingIconColor
//                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }
}