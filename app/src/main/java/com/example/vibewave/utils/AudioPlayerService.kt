package com.example.vibewave.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import java.io.File

class AudioPlayerService(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var updateListener: ((currentPos: Int, duration: Int) -> Unit)? = null
    private var handler: Handler? = null
    private var progressRunnable: Runnable? = null

    fun setProgressUpdateListener(listener: (currentPos: Int, duration: Int) -> Unit) {
        this.updateListener = listener
    }

    fun playAudioFile(filePath: String,onCompletion:()-> Unit) {
        cleanup()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.fromFile(File(filePath)))
            prepareAsync()
            setOnPreparedListener { mp ->
                start()
                startProgressUpdates(mp)
            }
            setOnCompletionListener {
                onCompletion()
                stopProgressUpdates()
                updateListener?.invoke(0, duration)
            }
        }
    }

   fun stopProgressUpdates() {
        progressRunnable?.let { handler?.removeCallbacks(it) }
    }
    fun cleanup() {
        stopProgressUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun startProgressUpdates(mp: MediaPlayer) {
        handler = Handler(Looper.getMainLooper())
        progressRunnable = object : Runnable {
            override fun run() {
                try {
                    if (mp.isPlaying) {
                        updateListener?.invoke(mp.currentPosition, mp.duration)
                    }
                    handler?.postDelayed(this, 100)
                } catch (e: IllegalStateException) {
                    // Player was released
                    stopProgressUpdates()
                }
            }
        }
        progressRunnable?.let { handler?.post(it) }
    }
    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position.coerceIn(0, mediaPlayer?.duration ?: 0))
    }
    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        releasePlayer()
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}



