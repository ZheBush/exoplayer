package com.example.exo_project.waveform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.example.exo_project.ui.theme.Green198
import com.example.exo_project.ui.theme.Green82
import com.example.exo_project.ui.theme.Grey224

@Composable
fun WaveformVisualizer(
    wavBytes: ByteArray,
    isPlaying: Boolean = false,
    progress: Float = 0f
) {
    val waveformData = remember(wavBytes) {
        parseWavData(wavBytes)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Waveform(
            amplitudes = waveformData.amplitudes,
            isPlaying = isPlaying,
            progress = progress
        )
    }
}

@Composable
fun Waveform(
    amplitudes: List<Float>,
    isPlaying: Boolean,
    progress: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        drawRect(Grey224)

        drawWaveformLine(amplitudes, width, height, centerY)
        if (isPlaying) {
            drawProgressIndicator(width, progress)
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
        val y = centerY + amplitudes[i] * height * 0.4f
        path.lineTo(x, y)
    }

    drawPath(
        path = path,
        color = Green82,
        style = Stroke(
            width = 2f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )

    path.lineTo(width, centerY)
    path.lineTo(0f, centerY)
    path.close()

}

private fun DrawScope.drawProgressIndicator(width: Float, progress: Float) {
    val progressX = width * progress

    drawLine(
        color = Green198,
        start = Offset(progressX, 0f),
        end = Offset(progressX, size.height),
        strokeWidth = 3f
    )

}