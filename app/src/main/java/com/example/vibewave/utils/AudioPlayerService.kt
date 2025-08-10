package com.example.vibewave.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File

class AudioPlayerService(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var visualizer : Visualizer? = null
    private var updateListener: ((currentPos: Int, duration: Int) -> Unit)? = null
    private var handler: Handler? = null
    private var progressRunnable: Runnable? = null

    fun setProgressUpdateListener(listener: (currentPos: Int, duration: Int) -> Unit) {
        this.updateListener = listener
    }

    fun playAudioFile(filePath: String,onAmplitudesUpdate:(amplitudes:List<Float>)->Unit) {
        cleanup()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, Uri.fromFile(File(filePath)))
            prepareAsync()
            setOnPreparedListener { mp ->
                // Initialize Visualizer only after MediaPlayer is prepared
                initVisualizer(mp.audioSessionId,onAmplitudesUpdate=onAmplitudesUpdate)
                start()
                startProgressUpdates(mp)
            }
            setOnCompletionListener {
                stopProgressUpdates()
                updateListener?.invoke(0, duration)
            }
        }
    }

    private fun initVisualizer(audioSessionId: Int,onAmplitudesUpdate:(amplitudes:List<Float>)->Unit) {
        try {
            visualizer = Visualizer(audioSessionId).apply {
                // Use recommended capture size instead of max
                captureSize = Visualizer.getCaptureSizeRange()[0] // Start with minimum

                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(
                        visualizer: Visualizer,
                        waveform: ByteArray,
                        samplingRate: Int
                    ) {
                        val amplitudes = waveform.map { it.toInt() and 0xFF }
                        println("33333333333333333333333333")
                        amplitudes.map{amp->println(amp)}
                        println("333333333333333333333333333")
                    }

                    override fun onFftDataCapture(
                        visualizer: Visualizer,
                        fft: ByteArray,
                        samplingRate: Int
                    ) {
                        println("##############################################")
                        fft.map{amp->println(amp)}
                        onAmplitudesUpdate(fft.map(transform = {byte->byte.toFloat()}))
                        println("##############################################")
                    }
                }, Visualizer.getMaxCaptureRate() / 2, false, true) // Reduced rate

                enabled = true
            }
        } catch (e: RuntimeException) {
            Log.e("Visualizer", "Error initializing visualizer", e)
            // Handle error or provide fallback
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