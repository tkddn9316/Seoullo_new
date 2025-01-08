package com.app.seoullo_new.view.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.seoullo_new.R
import com.app.seoullo_new.utils.Constants.SELECTED_NEARBY_LIST
import com.app.seoullo_new.utils.Constants.SELECTED_TOUR_LIST
import com.app.seoullo_new.view.ui.theme.primaryLight
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.BalloonWindow
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.compose.setBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeoulloAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationClick: () -> Unit,
    showAction: Boolean,
    onActionClick: (Int) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    var isOpenBalloon by rememberSaveable { mutableStateOf(true) }
    val builder = rememberBalloonBuilder {
        setCircularDuration(500)
        setArrowSize(10)
        setArrowOrientation(ArrowOrientation.BOTTOM)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setIsVisibleArrow(true)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBackgroundColor(primaryLight)
        setBalloonAnimation(BalloonAnimation.OVERSHOOT)
        setFocusable(false)
    }
    var balloonWindow: BalloonWindow? by remember { mutableStateOf(null) }

    Column {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    modifier = modifier.padding(4.dp),
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                IconButton(
                    modifier = modifier.padding(4.dp),
                    onClick = onNavigationClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                if (showAction) {
                    Balloon(
                        builder = builder,
                        onBalloonWindowInitialized = {
                            if (isOpenBalloon) balloonWindow = it
                        },
                        onComposedAnchor = {
                            balloonWindow?.showAlignBottom()
                            isOpenBalloon = false   // 최초 1번만 오픈
                        },
                        balloonContent = {
                            Text(
                                text = stringResource(R.string.balloon_info_text),
                                color = Color.White
                            )
                        }
                    ) { balloonWindow ->
                        IconButton(
                            modifier = Modifier.padding(4.dp),
                            onClick = { isMenuExpanded = true }
                        ) {
                            Icon(
                                imageVector = Icons.Sharp.Menu,
                                contentDescription = null
                            )

                            DropdownMenu(
                                modifier = Modifier.wrapContentSize(),
                                expanded = isMenuExpanded,
                                onDismissRequest = { isMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Map,
                                                contentDescription = stringResource(id = R.string.tour_list),
                                                modifier = Modifier.padding(12.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = stringResource(id = R.string.tour_list),
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.tour_list_description),
                                                    fontSize = 8.sp,
                                                    lineHeight = 8.sp * 1.2f
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onActionClick(SELECTED_TOUR_LIST)
                                        isMenuExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocationSearching,
                                                contentDescription = stringResource(id = R.string.nearby_list),
                                                modifier = Modifier.padding(12.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = stringResource(id = R.string.nearby_list),
                                                    fontSize = 10.sp
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.nearby_list_description),
                                                    fontSize = 8.sp,
                                                    lineHeight = 8.sp * 1.2f
                                                )
                                            }

                                        }
                                    },
                                    onClick = {
                                        onActionClick(SELECTED_NEARBY_LIST)
                                        isMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}