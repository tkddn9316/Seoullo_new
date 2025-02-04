package com.app.seoullo_new.view.main.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val descriptionColor = MaterialTheme.colorScheme.onBackground

    if (showLeadingIcon) {
        ListItem(
            modifier = clickableModifier,
            headlineContent = {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    color = descriptionColor
                )
            },
            supportingContent = {
                description?.let {
                    BasicText(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp,
                            maxFontSize = 14.sp,
                            stepSize = 2.sp
                        ),
                        color = { descriptionColor }
                    )
                }
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
            headlineContent = {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    color = descriptionColor
                )
            },
            supportingContent = {
                description?.let {
                    BasicText(
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp,
                            maxFontSize = 14.sp,
                            stepSize = 2.sp
                        ),
                        color = { descriptionColor }
                    )
                }
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