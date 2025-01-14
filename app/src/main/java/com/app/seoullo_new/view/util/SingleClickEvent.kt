package com.app.seoullo_new.view.util

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce

interface SingleClickEventInterface {
    fun event(event: () -> Unit)
}

@OptIn(FlowPreview::class)
@Composable
fun <T> singleClickEvent(
    // Interface를 변수로 사용하여 지정된 함수를 Override하여 Compose 함수에 적용시킨다.
    content: @Composable (SingleClickEventInterface) -> T
): T {
    val debounceState = remember {
        MutableSharedFlow<() -> Unit>(
            replay = 0, // 캐시의 크기
            extraBufferCapacity = 1,    // 버퍼 크기
            onBufferOverflow = BufferOverflow.DROP_OLDEST   // 오래된 것부터 버린다.
        )
    }

    val result = content(
        object : SingleClickEventInterface {
            // 매개 변수로 받은 event를 실행시킨다. tryEmit을 통해 함수를 실행시킨다.
            // emit는 suspend 환경에서만 사용이 가능하고, tryEmit은 suspend 환경이 아닐 때 사용이 가능하다.
            override fun event(event: () -> Unit) {
                debounceState.tryEmit(event)
            }
        }
    )

    LaunchedEffect(true) {
        debounceState
            // 0.5초의 시간동안 추가적인 입력이 없으면 가장 마지막의 이벤트를 발생시킨다.
            .debounce(500L)
            .collect { onClick ->
                onClick.invoke()
            }
    }

    return result
}

fun Modifier.singleClickable(
    onClick: () -> Unit,
) = composed {
    singleClickEvent { manager ->
        clickable(
            onClick = { manager.event { onClick() } },
        )
    }
}