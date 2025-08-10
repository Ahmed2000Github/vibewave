//package com.example.vibewave.utils
//
//import android.content.Context
//import java.io.File
//import android.graphics.Bitmap
//import android.media.MediaMetadataRetriever
//import android.media.MediaMetadataRetriever.OPTION_CLOSEST
//import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
//import android.media.MediaPlayer
//import android.media.audiofx.Visualizer
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.luminance
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//object WaveformUtils {
//    private const val SIMULATED_WAVES = 100
//    private const val SIMULATED_SEGMENTS = 5
//
//    suspend fun extractWaveformData(
//        context: Context,
//        filePath: String,
//        sampleCount: Int = 100
//    ): List<Float> = withContext(Dispatchers.IO) {
//        try {
//            // Try Visualizer first (most accurate)
//            tryExtractWithVisualizer(context, filePath, sampleCount)?.let {
//                return@withContext it
//            }
//
//            // Fallback to metadata frames (partial)
//            tryExtractWithMetadata(filePath, sampleCount)?.let {
//                return@withContext it
//            }
//
//            // Final fallback to simulated waveform
//            return@withContext generateSimulatedWaveform(sampleCount)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            generateSimulatedWaveform(sampleCount)
//        }
//    }
//
//    private fun tryExtractWithVisualizer(
//        context: Context,
//        filePath: String,
//        sampleCount: Int
//    ): List<Float>? {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return null
//
//        return try {
//            val mediaPlayer = MediaPlayer().apply {
//                setDataSource(filePath)
//                prepare()
//            }
//
//            Visualizer(mediaPlayer.audioSessionId).use { visualizer ->
//                visualizer.captureSize = Visualizer.getCaptureSizeRange()[1]
//                val waveform = ByteArray(visualizer.captureSize)
//
//                var success = false
//                val lock = Object()
//
//                visualizer.setDataCaptureListener(
//                    object : Visualizer.OnDataCaptureListener {
//                        override fun onWaveFormDataCapture(
//                            visualizer: Visualizer,
//                            bytes: ByteArray,
//                            samplingRate: Int
//                        ) {
//                            System.arraycopy(bytes, 0, waveform, 0, bytes.size)
//                            synchronized(lock) {
//                                success = true
//                                lock.notify()
//                            }
//                        }
//
//                        override fun onFftDataCapture(
//                            visualizer: Visualizer,
//                            fft: ByteArray,
//                            samplingRate: Int
//                        ) {}
//                    },
//                    Visualizer.getMaxCaptureRate(),
//                    false,
//                    true
//                )
//
//                visualizer.enabled = true
//
//                synchronized(lock) {
//                    if (!success) lock.wait(1000)
//                }
//
//                if (!success) return null
//
//                // Process waveform data
//                val amplitudes = mutableListOf<Float>()
//                val step = waveform.size / sampleCount
//
//                for (i in 0 until sampleCount) {
//                    val pos = i * step
//                    if (pos < waveform.size) {
//                        amplitudes.add((waveform[pos].toInt() and 0xFF) / 255f)
//                    } else {
//                        amplitudes.add(0f)
//                    }
//                }
//
//                return amplitudes.normalize()
//            }
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    private fun tryExtractWithMetadata(
//        filePath: String,
//        sampleCount: Int
//    ): List<Float>? {
//        return try {
//            MediaMetadataRetriever().use { retriever ->
//                retriever.setDataSource(filePath)
//                val duration = retriever.extractMetadata(METADATA_KEY_DURATION)?.toLong() ?: 0L
//
//                if (duration <= 0) return null
//
//                val waveform = mutableListOf<Float>()
//                val interval = duration / sampleCount
//
//                for (i in 0 until sampleCount) {
//                    val timeUs = i * interval * 1000
//                    val frame = retriever.getFrameAtTime(timeUs, OPTION_CLOSEST)
//                    waveform.add(frame?.let { calculateFrameAmplitude(it) } ?: 0f)
//                }
//
//                return waveform.normalize()
//            }
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    private fun calculateFrameAmplitude(bitmap: Bitmap): Float {
//        val pixels = IntArray(bitmap.width * bitmap.height)
//        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//        return pixels.maxOf { Color.red(it) } / 255f
//    }
//
//    private fun generateSimulatedWaveform(sampleCount: Int): List<Float> {
//        return List(sampleCount) { i ->
//            val pos = i.toFloat() / sampleCount
//            when {
//                pos < 0.2 -> sin(pos * 10f).absoluteValue * 0.8f
//                pos < 0.5 -> 0.3f + (pos - 0.2f) * 1.5f
//                pos < 0.8 -> 0.9f - (pos - 0.5f) * 2f
//                else -> (1f - pos) * 0.5f
//            }.coerceIn(0.1f, 1f)
//        }
//    }
//
//    private fun List<Float>.normalize(): List<Float> {
//        val max = maxOrNull() ?: 1f
//        return if (max > 0) map { it / max } else this
//    }
//}