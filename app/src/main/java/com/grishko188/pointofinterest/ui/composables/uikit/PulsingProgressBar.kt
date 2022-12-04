package com.grishko188.pointofinterest.ui.composables.uikit

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val DefaultAnimationDuration = 1000
private const val DefaultAnimationDelay = 300

private const val DefaultBallCount = 3
private val DefaultDiameter = 40.dp

@Composable
fun PulsingProgressBar(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = DefaultAnimationDuration,
    animationDelay: Int = DefaultAnimationDelay,
    diameter: Dp = DefaultDiameter,
    ballCount: Int = DefaultBallCount
) {
    val transition = rememberInfiniteTransition()
    val duration = animationDuration + animationDelay

    val fraction = mutableListOf<Float>().apply {
        for (i in 0 until ballCount) {
            val delay = if (ballCount > 1) animationDelay / (ballCount - 1) * i else 0
            val fraction by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = duration
                        0f at delay with LinearOutSlowInEasing
                        1f at animationDuration + delay
                        1f at duration
                    }
                )
            )
            add(fraction)
        }
    }

    ProgressIndicator(modifier, diameter) {
        drawIndeterminateBallScaleMultipleIndicator(
            diameter = fraction.map { lerp(0.dp.toPx(), diameter.toPx(), it) },
            alpha = fraction.map { lerp(1f, 0f, it) },
            color = color
        )
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + ((stop - start) * fraction)
}

private fun DrawScope.drawIndeterminateBallScaleMultipleIndicator(
    diameter: List<Float>,
    alpha: List<Float>,
    color: Color
) {
    for (i in diameter.indices) {
        drawCircle(
            color = color,
            radius = diameter[i] / 2,
            alpha = alpha[i]
        )
    }
}

@Composable
internal fun ProgressIndicator(
    modifier: Modifier,
    size: Dp,
    onDraw: DrawScope.() -> Unit
) {
    Canvas(
        modifier = modifier
            .progressSemantics()
            .size(size)
            .focusable(),
        onDraw = onDraw
    )
}


@Preview
@Composable
fun ProgressBarPreview() {
    PulsingProgressBar()
}