package com.app.seoullo_new.view.util

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.seoullo_new.view.ui.theme.notosansFont

@Preview
@Composable
fun FloatingButton() {
    FloatingActionButton(
        onClick = {},
        shape = CircleShape,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun ViewMultiFloatingButton() {
    MultipleFloatingActionButton(
        fabIcon = Icons.Default.Add,
        items = arrayListOf(FabItem(icon = Icons.Default.Add, label = "", {}))
    )
}

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

class FabItem(
    val icon: ImageVector,
    val label: String,
    val onFabItemClicked: () -> Unit
)

/** 세부 메뉴가 있는 FAB */
@Composable
fun MultipleFloatingActionButton(
    fabIcon: ImageVector,
    items: List<FabItem>,
    showLabels: Boolean = true,
    onStateChanged: ((state: MultiFabState) -> Unit)? = null
) {
    var currentState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    val stateTransition: Transition<MultiFabState> =
        updateTransition(targetState = currentState, label = "")
    val stateChange: () -> Unit = {
        currentState = if (stateTransition.currentState == MultiFabState.EXPANDED) {
            MultiFabState.COLLAPSED
        } else MultiFabState.EXPANDED
        onStateChanged?.invoke(currentState)
    }
    val rotation: Float by stateTransition.animateFloat(
        transitionSpec = {
            if (targetState == MultiFabState.EXPANDED) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }
    val isEnable = currentState == MultiFabState.EXPANDED

    BackHandler(isEnable) {
        currentState = MultiFabState.COLLAPSED
    }

    val modifier = if (currentState == MultiFabState.EXPANDED)
        Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                currentState = MultiFabState.COLLAPSED
            } else Modifier.fillMaxSize()

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
            ) {
                items.forEach { item ->
                    SmallFloatingActionButtonRow(
                        item = item,
                        stateTransition = stateTransition,
                        showLabel = showLabels,
                        onItemClicked = {
                            currentState = MultiFabState.COLLAPSED
                            item.onFabItemClicked()
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        stateChange()
                    }) {
                    Icon(
                        imageVector = fabIcon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }

        }
    }
}

@Composable
fun SmallFloatingActionButtonRow(
    item: FabItem,
    showLabel: Boolean,
    stateTransition: Transition<MultiFabState>,
    onItemClicked: () -> Unit
) {
    val alpha: Float by stateTransition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 50)
        }, label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val scale: Float by stateTransition.animateFloat(
        label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1.0f else 0f
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(animateFloatAsState((alpha), label = "").value)
            .scale(animateFloatAsState(targetValue = scale, label = "").value)
    ) {
        if (showLabel) {
            Box(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Text(
                    text = item.label,
                    fontFamily = notosansFont,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
                )
            }
        }
        SmallFloatingActionButton(
            shape = CircleShape,
            modifier = Modifier
                .padding(4.dp),
            onClick = { onItemClicked() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label
            )
        }
    }
}