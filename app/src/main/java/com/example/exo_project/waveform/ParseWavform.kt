package com.example.exo_project.waveform

import android.content.Context
import android.util.Log
import java.io.InputStream

data class WaveformData(
    val amplitudes: List<Float>,
    val sampleRate: Int,
    val channels: Int,
    val bitsPerSample: Int
)

fun readAudioData(inputStream: InputStream): ByteArray {
    return inputStream.use { it.readBytes() }
}

fun parseWavData(wavBytes: ByteArray): WaveformData {

    val sampleRate = (wavBytes[24].toInt() and 0xFF) or
            (wavBytes[25].toInt() and 0xFF shl 8) or
            (wavBytes[26].toInt() and 0xFF shl 16) or
            (wavBytes[27].toInt() and 0xFF shl 24)

//    val channels = (wavBytes[22].toInt() and 0xFF) or
//            (wavBytes[23].toInt() and 0xFF shl 8)

    val channels = 2

    val bitsPerSample = (wavBytes[34].toInt() and 0xFF) or
            (wavBytes[35].toInt() and 0xFF shl 8)

    val dataStart = findDataChunk(wavBytes)
    val audioData = wavBytes.copyOfRange(dataStart, wavBytes.size)
    val amplitudes = decodeAmplitudes(audioData, channels, 1000)

    return WaveformData(amplitudes, sampleRate, channels, bitsPerSample)
}

fun findDataChunk(wavBytes: ByteArray): Int {
    for (i in 0 until wavBytes.size - 4) {
        if (wavBytes[i] == 'd'.code.toByte() &&
            wavBytes[i + 1] == 'a'.code.toByte() &&
            wavBytes[i + 2] == 't'.code.toByte() &&
            wavBytes[i + 3] == 'a'.code.toByte()) {
            return i + 8
        }
    }
    return 44
}

fun decodeAmplitudes(audioData: ByteArray, channels: Int, samples: Int): List<Float> {
    val amplitudes = mutableListOf<Float>()
    val bytesPerSample = 2 * channels
    val totalSamples = audioData.size / bytesPerSample
    val step = (totalSamples / samples).coerceAtLeast(1)

    for (sampleIndex in 0 until totalSamples step step) {
        if (amplitudes.size >= samples) break

        val byteIndex = sampleIndex * bytesPerSample

        if (byteIndex + 1 >= audioData.size) break

        val sample = ((audioData[byteIndex + 1].toInt() and 0xFF shl 8) or
                (audioData[byteIndex].toInt() and 0xFF)).toShort()
        val normalized = sample.toFloat() / Short.MAX_VALUE.toFloat()

        amplitudes.add(normalized.coerceIn(-1f, 1f))
    }

    return amplitudes
}
