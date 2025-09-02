package com.example.vibewave.data.local.mediasource

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.vibewave.data.local.entities.MusicFileEntity
import com.example.vibewave.utils.AppUtils
import java.io.File


class DeviceMusicSource(
    private val contentResolver: ContentResolver
) {
    @RequiresApi(Build.VERSION_CODES.N)
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
                val filePath = cursor.getString(4)
                if (filePath != null && File(filePath).exists() && cursor.getLong(3) != 0L) {
                        songs.add(
                            MusicFileEntity(
                                id = cursor.getLong(0).toString(),
                                title = cursor.getString(1) ?: "Unknown",
                                artist = cursor.getString(2) ?: "Unknown",
                                duration = cursor.getLong(3),
                                filePath = cursor.getString(4),
                                thumbnail = extractThumbnailFromMp3(cursor.getString(4)),
                                        drawableThumbnail = AppUtils.getRandomImage()
                            )
                        )

                }

            }
        }
        return songs
    }
    private fun extractThumbnailFromMp3(filePath: String): String? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val embeddedPicture = retriever.embeddedPicture

            embeddedPicture?.let { imageData ->
                // Use system temp directory (doesn't require Context)
                val tempFile = File.createTempFile(
                    "thumb_${System.currentTimeMillis()}",
                    ".jpg"
                )

                tempFile.outputStream().use { output ->
                    output.write(imageData)
                }

                tempFile.absolutePath
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }
}