package com.app.seoullo_new.view.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConvergeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    pressedColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.90f),
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 3.dp,

    // ↓↓↓ 효과 증폭 (전역)
    pressedScale: Float = 0.96f,                   // 버튼 자체 축소 폭 키움 (기존 0.98 → 0.96)
    labelMinScaleX: Float = 0.93f,                 // 라벨 가로 축소 (쥐어짜기)
    labelMinScaleY: Float = 0.97f,                 // 라벨 세로 축소
    labelMaxNegativeLetterSpacing: TextUnit = (-0.1).sp, // 자간 압축(음수일수록 빡빡)

    horizontalPadding: Dp = 20.dp,
    verticalPadding: Dp = 12.dp,
    leading: (@Composable () -> Unit)? = null,
    label: @Composable () -> Unit,
    trailing: (@Composable () -> Unit)? = null,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()

    // 0→1로 가는 진행도
    val pressProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "pressProgress"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressedScale else 1f,
        animationSpec = spring(),
        label = "scale"
    )

    val bg by animateColorAsState(
        targetValue = if (isPressed) pressedColor else containerColor,
        animationSpec = tween(120),
        label = "bgColor"
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .clickable(
                enabled = enabled,
                interactionSource = interaction,
                indication = LocalIndication.current,
                onClick = onClick
            ),
        color = bg,
        contentColor = contentColor,
        shape = shape,
        tonalElevation = elevation
    ) {
        ConvergeRow(
            pressProgress = pressProgress,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding,

            // ↓↓↓ 증폭 파라미터 전달
            labelMinScaleX = labelMinScaleX,
            labelMinScaleY = labelMinScaleY,
            labelMaxNegativeLetterSpacing = labelMaxNegativeLetterSpacing,

            leading = leading,
            label = label,
            trailing = trailing
        )
    }
}

@Composable
private fun ConvergeRow(
    pressProgress: Float,
    horizontalPadding: Dp,
    verticalPadding: Dp,

    // ↓↓↓ 라벨 효과 증폭용 추가 파라미터
    labelMinScaleX: Float,
    labelMinScaleY: Float,
    labelMaxNegativeLetterSpacing: TextUnit,

    leading: (@Composable () -> Unit)?,
    label: @Composable () -> Unit,
    trailing: (@Composable () -> Unit)?
) {
    val density = LocalDensity.current

    fun lerpFloat(a: Float, b: Float, t: Float) = a + (b - a) * t

    SubcomposeLayout { constraints ->
        val hPadPx = with(density) { horizontalPadding.roundToPx() }
        val vPadPx = with(density) { verticalPadding.roundToPx() }

        // --- Leading 측정
        val leadingPlaceables = if (leading != null) {
            subcompose("leading", leading).map { it.measure(Constraints()) }
        } else emptyList()

        // --- Label(라벨 압축 + 스케일 적용) 측정
        val labelPlaceables = subcompose("label") {
            // 프레스 진행도에 따라 자간/스케일 계산
            val ls = with(density) {
                lerpFloat(0f, labelMaxNegativeLetterSpacing.value, pressProgress).sp
            }
            val sx = lerpFloat(1f, labelMinScaleX, pressProgress)
            val sy = lerpFloat(1f, labelMinScaleY, pressProgress)

            // ProvideTextStyle로 letterSpacing 주입 (라벨 Text에 직접 letterSpacing 안주면 이 값 먹힘)
            Box(
                Modifier.graphicsLayer {
                    // 글자 자체를 압축: X는 더 강하게, Y는 살짝
                    scaleX = sx
                    scaleY = sy
                    transformOrigin = TransformOrigin.Center
                }
            ) {
                ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(letterSpacing = ls)) {
                    label()
                }
            }
        }.map {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        // --- Trailing 측정
        val trailingPlaceables = if (trailing != null) {
            subcompose("trailing", trailing).map { it.measure(Constraints()) }
        } else emptyList()

        val leadingW = leadingPlaceables.maxOfOrNull { it.width } ?: 0
        val labelW = labelPlaceables.maxOfOrNull { it.width } ?: 0
        val trailingW = trailingPlaceables.maxOfOrNull { it.width } ?: 0
        val contentH = listOf(
            leadingPlaceables.maxOfOrNull { it.height } ?: 0,
            labelPlaceables.maxOfOrNull { it.height } ?: 0,
            trailingPlaceables.maxOfOrNull { it.height } ?: 0
        ).maxOrNull() ?: 0

        val width = constraints.maxWidth.coerceAtLeast(leadingW + labelW + trailingW + hPadPx * 2)
        val height = (contentH + vPadPx * 2).coerceIn(constraints.minHeight, constraints.maxHeight)

        // 기본 위치
        val leadingBaseX = hPadPx
        val trailingBaseX = width - trailingW - hPadPx
        val labelBaseX = (width - labelW) / 2

        // 목표(완전 중앙)
        fun centerX(w: Int) = (width - w) / 2
        val leadingTargetX = centerX(leadingW)
        val labelTargetX = centerX(labelW)
        val trailingTargetX = centerX(trailingW)

        // 프레스 때 아이콘들도 더 과감하게 당기고 싶으면 거리 보간 강도를 키우자(비선형)
        fun easeOutCubic(t: Float) = 1f - (1f - t) * (1f - t) * (1f - t)
        val t = easeOutCubic(pressProgress)

        fun lerpInt(a: Int, b: Int, t: Float) = (a + (b - a) * t).toInt()
        val lX = lerpInt(leadingBaseX, leadingTargetX, t)
        val cX = lerpInt(labelBaseX, labelTargetX, t)
        val rX = lerpInt(trailingBaseX, trailingTargetX, t)
        val centerY = (height - contentH) / 2

        layout(width, height) {
            leadingPlaceables.forEach { it.placeRelative(lX, centerY) }
            labelPlaceables.forEach { it.placeRelative(cX, centerY) }
            trailingPlaceables.forEach { it.placeRelative(rX, centerY) }
        }
    }
}