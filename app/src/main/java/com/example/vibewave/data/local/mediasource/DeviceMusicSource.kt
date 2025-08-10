package com.example.vibewave.data.local.mediasource

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.example.vibewave.data.local.entities.MusicFileEntity


class DeviceMusicSource(
    private val contentResolver: ContentResolver
) {
    fun getAllDeviceSongs(): List<MusicFileEntity> {
        val songs = mutableListOf<MusicFileEntity>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->

            while (cursor.moveToNext()) {
                    println("______________________________________________________")
                for (i in 0..6) {
                    println(cursor.getString(i))
                }
                    println("______________________________________________________")
                songs.add(
                    MusicFileEntity(
                        id = cursor.getLong(0).toString(),
                        title = cursor.getString(1) ?: "Unknown",
                        artist = cursor.getString(2) ?: "Unknown",
                        duration = cursor.getLong(3),
                        filePath = cursor.getString(4),
                        thumbnail = extractThumbnailFromMp3(cursor.getString(4))
                    )
                )
            }
        }
        return songs
    }
    private fun extractThumbnailFromMp3(filePath: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            retriever.embeddedPicture // Returns ByteArray or null
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }
}