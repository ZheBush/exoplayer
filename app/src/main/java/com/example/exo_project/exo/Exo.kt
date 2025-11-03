package com.example.exo_project.exo

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun createExoPlayer(context: Context): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_OFF
    }
}

fun playSound(exoPlayer: ExoPlayer, context: Context, soundResId: Int) {
    exoPlayer.stop()

    val rawResourceUri = "android.resource://${context.packageName}/$soundResId".toUri()
    val mediaItem = MediaItem.fromUri(rawResourceUri)

    exoPlayer.setMediaItem(mediaItem)
    exoPlayer.prepare()
    exoPlayer.play()
}

fun getWavDuration(context: Context, resId: Int): Long? {
    val retriever = MediaMetadataRetriever()
    return try {
        val assetFileDescriptor = context.resources.openRawResourceFd(resId)
        retriever.setDataSource(
            assetFileDescriptor.fileDescriptor,
            assetFileDescriptor.startOffset,
            assetFileDescriptor.length
        )
        assetFileDescriptor.close()

        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        durationString?.toLongOrNull()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}