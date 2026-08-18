package com.han.konnect.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.han.konnect.data.model.SkillScore
import com.han.konnect.ui.theme.PurpleMain
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadarChart(
    scores: List<SkillScore>,
    modifier: Modifier = Modifier,
    maxScore: Float = 100f
) {
    check(scores.size >= 3) { "Radar chart requires at least 3 categories" }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = (size.minDimension / 2f) * 0.75f
        val numPoints = scores.size
        val angleStep = (2 * Math.PI / numPoints).toFloat()
        val startAngle = -Math.PI.toFloat() / 2f


        val gridLevels = 3
        for (level in 1..gridLevels) {
            val levelRadius = radius * (level.toFloat() / gridLevels)
            val gridPath = Path()

            for (i in 0 until numPoints) {
                val angle = startAngle + i * angleStep
                val x = center.x + levelRadius * cos(angle)
                val y = center.y + levelRadius * sin(angle)

                if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
            }
            gridPath.close()
            drawPath(
                path = gridPath,
                color = Color.LightGray.copy(alpha = 0.5f),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        for (i in 0 until numPoints) {
            val angle = startAngle + i * angleStep
            val x = center.x + radius * cos(angle)
            val y = center.y + radius * sin(angle)
            drawLine(
                color = Color.LightGray.copy(alpha = 0.5f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.dp.toPx()
            )
        }

        val scorePath = Path()
        for (i in 0 until numPoints) {
            val angle = startAngle + i * angleStep
            val scoreRatio = (scores[i].score / maxScore).coerceIn(0f, 1f)
            val pointRadius = radius * scoreRatio
            val x = center.x + pointRadius * cos(angle)
            val y = center.y + pointRadius * sin(angle)

            if (i == 0) scorePath.moveTo(x, y) else scorePath.lineTo(x, y)
        }
        scorePath.close()

        drawPath(
            path = scorePath,
            color = PurpleMain.copy(alpha = 0.35f)
        )

        drawPath(
            path = scorePath,
            color = PurpleMain,
            style = Stroke(width = 2.5.dp.toPx())
        )

        for (i in 0 until numPoints) {
            val angle = startAngle + i * angleStep
            val labelRadius = radius + 24.dp.toPx()
            val x = center.x + labelRadius * cos(angle)
            val y = center.y + labelRadius * sin(angle)

            drawContext.canvas.nativeCanvas.drawText(
                "${scores[i].category} (${scores[i].score})",
                x,
                y + 10f,
                Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 32f
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }
    }
}