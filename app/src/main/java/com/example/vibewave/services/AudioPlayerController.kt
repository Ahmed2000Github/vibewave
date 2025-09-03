package com.example.vibewave.services

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vibewave.domain.models.Song
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class AudioPlayerController(private val context: Context) {

    private var progressListener: ((currentPos: Int, duration: Int) -> Unit)? = null
    private var  onCompletion: (() -> Unit)? = null



    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                AudioPlayerServiceSession.ACTION_PROGRESS_UPDATE -> {
                    val currentPos = intent.getIntExtra(AudioPlayerServiceSession.EXTRA_CURRENT_POS, 0)
                    val duration = intent.getIntExtra(AudioPlayerServiceSession.EXTRA_DURATION, 0)
                    progressListener?.invoke(currentPos, duration)
                }
                AudioPlayerServiceSession.ACTION_ON_COMPLETION ->{
                    onCompletion?.invoke()
                }
            }
        }
    }

    init {
        val filter = IntentFilter().apply {
            addAction(AudioPlayerServiceSession.ACTION_PROGRESS_UPDATE)
            addAction(AudioPlayerServiceSession.ACTION_ON_COMPLETION)
        }
        context.registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED)
    }
    fun setLoopCount(count: Int) {
        val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
            action = "ACTION_SET_LOOP_COUNT"
            putExtra("EXTRA_LOOP_COUNT", count)
        }
        context.startService(intent)
    }

    fun setProgressUpdateListener(listener: (currentPos: Int, duration: Int) -> Unit) {
        this.progressListener = listener
    }
    fun setOnCompletion( onCompletion: () -> Unit) {
        this.onCompletion = onCompletion
    }
    fun isServiceRunning(): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any { service ->
            service.service.className == AudioPlayerServiceSession::class.java.name
        }
    }
    fun playAudio(filePath: String) {
            val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
                action = AudioPlayerServiceSession.ACTION_PLAY
                putExtra(AudioPlayerServiceSession.EXTRA_FILE_PATH, filePath)
            }
            context.startService(intent)
//        }

    }

    fun pause() {
        val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
            action = AudioPlayerServiceSession.ACTION_PAUSE
        }
        context.startService(intent)
    }
  fun resume() {

        val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
            action = AudioPlayerServiceSession.ACTION_RESUME
        }
        context.startService(intent)
    }

    fun stop() {
        val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
            action = AudioPlayerServiceSession.ACTION_STOP
        }
        context.startService(intent)
    }

    fun seekTo(position: Int) {
        val intent = Intent(context, AudioPlayerServiceSession::class.java).apply {
            action = AudioPlayerServiceSession.ACTION_SEEK
            putExtra(AudioPlayerServiceSession.EXTRA_SEEK_POSITION, position)
        }
        context.startService(intent)
    }

    fun cleanup() {
        context.unregisterReceiver(broadcastReceiver)
    }
}