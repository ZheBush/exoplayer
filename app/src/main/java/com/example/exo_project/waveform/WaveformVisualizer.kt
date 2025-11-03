package com.example.exo_project.waveform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey224

@Composable
fun Waveform(
    amplitudes: List<Float>,
    isPlaying: Boolean,
    progress: Float,
    colorProgress: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val width = size.width
        val height = size.height
        val centerY = height / 2

        drawRect(Grey224)

        drawWaveformLine(amplitudes, width, height, centerY)
        if (isPlaying || progress > 0f) {
            drawProgressIndicator(width, progress, colorProgress)
        }

    }
}

private fun DrawScope.drawWaveformLine(
    amplitudes: List<Float>,
    width: Float,
    height: Float,
    centerY: Float
) {
    val path = Path()
    val pointWidth = width / (amplitudes.size - 1)

    path.moveTo(0f, centerY + amplitudes[0] * height * 0.4f)

    for (i in 1 until amplitudes.size) {
        val x = i * pointWidth
        val y = centerY + amplitudes[i] * height * 0.2f
        path.lineTo(x, y)
    }

    drawPath(
        path = path,
        color = Green82,
        style = Stroke(
            width = 1.5f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    path.lineTo(width, centerY)
    path.lineTo(0f, centerY)
    path.close()

}

private fun DrawScope.drawProgressIndicator(width: Float, progress: Float, colorProgress: Float) {

    val progressX = width * progress

    drawRect(
        color = Green198.copy(alpha = colorProgress),
        size = Size(progressX, size.height)
    )

}