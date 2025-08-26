//package com.example.vibewave.utils
//
//import android.app.*
//import android.content.Context
//import android.content.Intent
//import android.media.MediaPlayer
//import android.media.session.PlaybackState
//import android.net.Uri
//import android.os.*
//import android.support.v4.media.session.MediaSessionCompat
//import androidx.core.app.NotificationCompat
//import androidx.media.session.MediaButtonReceiver
//import java.io.File
//
//class AudioPlayerServiceSession : Service() {
//    private var mediaPlayer: MediaPlayer? = null
//    private var mediaSession: MediaSessionCompat? = null
//    private var handler: Handler? = null
//    private var progressRunnable: Runnable? = null
//
//    companion object {
//        const val ACTION_PLAY = "ACTION_PLAY"
//        const val ACTION_PAUSE = "ACTION_PAUSE"
//        const val ACTION_STOP = "ACTION_STOP"
//        const val ACTION_SEEK = "ACTION_SEEK"
//        const val EXTRA_FILE_PATH = "EXTRA_FILE_PATH"
//        const val EXTRA_SEEK_POSITION = "EXTRA_SEEK_POSITION"
//        const val NOTIFICATION_ID = 101
//        const val CHANNEL_ID = "audio_player_channel"
//    }
//
//    private var updateListener: ((currentPos: Int, duration: Int) -> Unit)? = null
//
//    fun setProgressUpdateListener(listener: (currentPos: Int, duration: Int) -> Unit) {
//        this.updateListener = listener
//    }
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        intent?.let { handleIntent(it) }
//        return START_STICKY
//    }
//
//    private fun handleIntent(intent: Intent) {
//        when (intent.action) {
//            ACTION_PLAY -> {
//                val filePath = intent.getStringExtra(EXTRA_FILE_PATH)
//                if (filePath != null) {
//                    playAudioFile(filePath) {
//                        stopForeground(true)
//                        stopSelf()
//                    }
//                }
//            }
//            ACTION_PAUSE -> pause()
//            ACTION_STOP -> stop()
//            ACTION_SEEK -> {
//                val position = intent.getIntExtra(EXTRA_SEEK_POSITION, 0)
//                seekTo(position)
//            }
//        }
//    }
//
//    fun playAudioFile(filePath: String, onCompletion: () -> Unit) {
//        cleanup()
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(applicationContext, Uri.fromFile(File(filePath)))
//            prepareAsync()
//            setOnPreparedListener { mp ->
//                start()
//                startProgressUpdates(mp)
//                updatePlaybackState(PlaybackState.STATE_PLAYING)
//                startForeground(NOTIFICATION_ID, createNotification())
//            }
//            setOnCompletionListener {
//                onCompletion()
//                stopProgressUpdates()
//                updateListener?.invoke(0, duration)
//                updatePlaybackState(PlaybackState.STATE_STOPPED)
//            }
//        }
//
//        // Initialize MediaSession
//        mediaSession = MediaSessionCompat(applicationContext, "AudioPlayerService").apply {
//            setCallback(object : MediaSessionCompat.Callback() {
//                override fun onPlay() {
//                    resume()
//                }
//
//                override fun onPause() {
//                    pause()
//                }
//
//                override fun onStop() {
//                    stop()
//                }
//
//                override fun onSeekTo(pos: Long) {
//                    seekTo(pos.toInt())
//                }
//            })
//            isActive = true
//        }
//    }
//
//    fun stopProgressUpdates() {
//        progressRunnable?.let { handler?.removeCallbacks(it) }
//    }
//
//    fun cleanup() {
//        stopProgressUpdates()
//        mediaPlayer?.release()
//        mediaPlayer = null
//        mediaSession?.release()
//        mediaSession = null
//    }
//
//    private fun startProgressUpdates(mp: MediaPlayer) {
//        handler = Handler(Looper.getMainLooper())
//        progressRunnable = object : Runnable {
//            override fun run() {
//                try {
//                    if (mp.isPlaying) {
//                        updateListener?.invoke(mp.currentPosition, mp.duration)
//                    }
//                    handler?.postDelayed(this, 100)
//                } catch (e: IllegalStateException) {
//                    stopProgressUpdates()
//                }
//            }
//        }
//        progressRunnable?.let { handler?.post(it) }
//    }
//
//    fun seekTo(position: Int) {
//        mediaPlayer?.seekTo(position.coerceIn(0, mediaPlayer?.duration ?: 0))
//    }
//
//    fun pause() {
//        mediaPlayer?.pause()
//        updatePlaybackState(PlaybackState.STATE_PAUSED)
//        stopForeground(false)
//    }
//
//    fun resume() {
//        mediaPlayer?.start()
//        updatePlaybackState(PlaybackState.STATE_PLAYING)
//        startForeground(NOTIFICATION_ID, createNotification())
//    }
//
//    fun stop() {
//        mediaPlayer?.stop()
//        updatePlaybackState(PlaybackState.STATE_STOPPED)
//        stopForeground(true)
//        releasePlayer()
//    }
//
//    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
//
//    private fun releasePlayer() {
//        mediaPlayer?.release()
//        mediaPlayer = null
//    }
//
//    private fun updatePlaybackState(state: Int) {
//        mediaSession?.setPlaybackState(
//            PlaybackState.Builder()
//                .setActions(
//                    PlaybackState.ACTION_PLAY or
//                            PlaybackState.ACTION_PAUSE or
//                            PlaybackState.ACTION_STOP or
//                            PlaybackState.ACTION_SEEK_TO
//                )
//                .setState(state, mediaPlayer?.currentPosition?.toLong() ?: 0, 1.0f)
//                .build()
//        )
//    }
//
//    private fun createNotification(): Notification {
//        createNotificationChannel()
//
//        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
//            .setContentTitle("Playing Music")
//            .setContentText("Now playing")
//            .setSmallIcon(android.R.drawable.ic_media_play)
//            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
//                .setMediaSession(mediaSession?.sessionToken)
//                .setShowActionsInCompactView(0, 1))
//            .addAction(
//                NotificationCompat.Action(
//                    android.R.drawable.ic_media_pause,
//                    "Pause",
//                    MediaButtonReceiver.buildMediaButtonPendingIntent(
//                        applicationContext,
//                        PlaybackState.ACTION_PAUSE
//                    )
//                )
//            )
//            .addAction(
//                NotificationCompat.Action(
//                    android.R.drawable.ic_media_play,
//                    "Play",
//                    MediaButtonReceiver.buildMediaButtonPendingIntent(
//                        applicationContext,
//                        PlaybackState.ACTION_PLAY
//                    )
//                )
//            )
//            .build()
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Audio Player",
//                NotificationManager.IMPORTANCE_LOW
//            ).apply {
//                description = "Audio playback controls"
//            }
//            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cleanup()
//    }
//}