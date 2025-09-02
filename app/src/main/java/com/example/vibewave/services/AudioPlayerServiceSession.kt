package com.example.vibewave.services

import android.R
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import java.io.File

class AudioPlayerServiceSession : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private var handler: Handler? = null
    private var progressRunnable: Runnable? = null
    private var loopCount = 0
    private var desiredLoops = 1

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_SEEK = "ACTION_SEEK"
        const val EXTRA_FILE_PATH = "EXTRA_FILE_PATH"
        const val EXTRA_SEEK_POSITION = "EXTRA_SEEK_POSITION"
        const val ACTION_SET_LOOP_COUNT = "ACTION_SET_LOOP_COUNT"
        const val EXTRA_LOOP_COUNT = "EXTRA_LOOP_COUNT"
        const val ACTION_ON_COMPLETION = "ACTION_ON_COMPLETION"
        const val ACTION_PROGRESS_UPDATE = "ACTION_PROGRESS_UPDATE"
        const val EXTRA_CURRENT_POS = "EXTRA_CURRENT_POS"
        const val EXTRA_DURATION = "EXTRA_DURATION"
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "audio_player_channel"
    }

    private var updateListener: ((currentPos: Int, duration: Int) -> Unit)? = null

    fun setProgressUpdateListener(listener: (currentPos: Int, duration: Int) -> Unit) {
        this.updateListener = listener
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handleIntent(it) }
        return START_STICKY
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            ACTION_PLAY -> {
                val filePath = intent.getStringExtra(EXTRA_FILE_PATH)
                if (filePath != null) {
                    playAudioFile(filePath) {
                        sendOnCompletionBroadcast()
                        stopForeground(true)
                        stopSelf()
                    }
                }
            }
            ACTION_PAUSE -> pause()
            ACTION_RESUME -> resume()
            ACTION_STOP -> stop()
            ACTION_SET_LOOP_COUNT -> {
                val count = intent.getIntExtra(EXTRA_LOOP_COUNT, 1)
                setLoopCount(count)
            }
            ACTION_SEEK -> {
                val position = intent.getIntExtra(EXTRA_SEEK_POSITION, 0)
                seekTo(position)
            }
        }
    }
    private fun setLoopCount(count: Int) {
        desiredLoops = count
        loopCount = 0
        mediaPlayer?.isLooping = count == Int.MAX_VALUE
    }


    @SuppressLint("ForegroundServiceType")
    fun playAudioFile(filePath: String, onCompletion: () -> Unit) {
        cleanup()
        loopCount = 0
        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, Uri.fromFile(File(filePath)))
            prepareAsync()
            setOnPreparedListener { mp ->
                start()
                startProgressUpdates(mp)
                updatePlaybackStateCompat(PlaybackStateCompat.STATE_PLAYING)
                startForeground(NOTIFICATION_ID, createNotification())
            }
            setOnCompletionListener {
                if (desiredLoops == Int.MAX_VALUE) {
                    return@setOnCompletionListener
                }
                if (loopCount < desiredLoops) {
                loopCount++
                    seekTo(0)
                    start()
                } else {
                onCompletion()
                stopProgressUpdates()
                updateListener?.invoke(0, duration)
                updatePlaybackStateCompat(PlaybackStateCompat.STATE_STOPPED)
            }
            }
        }

        // Initialize MediaSession
        mediaSession = MediaSessionCompat(applicationContext, "AudioPlayerServiceSession").apply {
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    resume()
                }

                override fun onPause() {
                    pause()
                }

                override fun onStop() {
                    stop()
                }

                override fun onSeekTo(pos: Long) {
                    seekTo(pos.toInt())
                }
            })
            isActive = true
        }
    }

    fun stopProgressUpdates() {
        progressRunnable?.let { handler?.removeCallbacks(it) }
    }

    fun cleanup() {
        stopProgressUpdates()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaSession?.release()
        mediaSession = null
    }

    private fun startProgressUpdates(mp: MediaPlayer) {
        handler = Handler(Looper.getMainLooper())
        progressRunnable = object : Runnable {
            override fun run() {
                try {
                    if (mp.isPlaying) {
                        // Send broadcast instead of using listener
                        sendProgressBroadcast(mp.currentPosition, mp.duration)
                    }
                    handler?.postDelayed(this, 100)
                } catch (e: IllegalStateException) {
                    stopProgressUpdates()
                }
            }
        }
        progressRunnable?.let { handler?.post(it) }
    }

    private fun sendOnCompletionBroadcast() {
        val intent = Intent(ACTION_ON_COMPLETION).apply {
            setPackage(applicationContext.packageName)
        }

        applicationContext.sendBroadcast(intent)
    }
    private fun sendProgressBroadcast(currentPos: Int, duration: Int) {
        val intent = Intent(ACTION_PROGRESS_UPDATE).apply {
            putExtra(EXTRA_CURRENT_POS, currentPos)
            putExtra(EXTRA_DURATION, duration)
            setPackage(applicationContext.packageName)
        }
        applicationContext.sendBroadcast(intent)
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position.coerceIn(0, mediaPlayer?.duration ?: 0))
    }

    fun pause() {
        mediaPlayer?.pause()
        updatePlaybackStateCompat(PlaybackStateCompat.STATE_PAUSED)
        stopForeground(false)
    }

    @SuppressLint("ForegroundServiceType")
    fun resume() {
        mediaPlayer?.start()
        updatePlaybackStateCompat(PlaybackStateCompat.STATE_PLAYING)
        startForeground(NOTIFICATION_ID, createNotification())
    }

    fun stop() {
        mediaPlayer?.stop()
        updatePlaybackStateCompat(PlaybackStateCompat.STATE_STOPPED)
        stopForeground(true)
        releasePlayer()
    }

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    private fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun updatePlaybackStateCompat(state: Int) {
        mediaSession?.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_STOP or
                            PlaybackStateCompat.ACTION_SEEK_TO
                )
                .setState(state, mediaPlayer?.currentPosition?.toLong() ?: 0, 1.0f)
                .build() as PlaybackStateCompat?
        )
    }

    private fun createNotification(): Notification {
        createNotificationChannel()

            return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setContentTitle("Playing Music")
                .setContentText("Now playing")
                .setSmallIcon(R.drawable.ic_media_play)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1))
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_media_pause,
                        "Pause",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_PAUSE
                        )
                    )
                )
                .addAction(
                    NotificationCompat.Action(
                        R.drawable.ic_media_play,
                        "Play",
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_PLAY
                        )
                    )
                )
                .build()
        }

        private fun startForegroundWithType() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIFICATION_ID, createNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            } else {
                startForeground(NOTIFICATION_ID, createNotification())
            }
        }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Audio Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Audio playback controls"
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanup()
    }
}

