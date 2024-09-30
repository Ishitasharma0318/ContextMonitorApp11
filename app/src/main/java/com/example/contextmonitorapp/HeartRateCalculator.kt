package com.example.contextmonitorapp

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlin.math.min

class HeartRateReader {

    // Existing read function that queries for a video in storage
    fun read(activity: Activity, context: Context): Int? {
        val contentResolver = context.contentResolver
        var rate: Int? = null

        val videoName = "Heart Rate.mp4" // Video name you are looking for
        val projection = arrayOf(android.provider.MediaStore.Video.Media._ID)
        val selection = "${android.provider.MediaStore.Video.Media.DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(videoName)

        // Request permissions
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0
        )

        // Query the MediaStore to get the URI of the video
        val videoUri: Uri? = contentResolver.query(
            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToNext()) {
                val idCol = cursor.getColumnIndex(android.provider.MediaStore.Video.Media._ID)
                val id = cursor.getLong(idCol)
                android.content.ContentUris.withAppendedId(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            } else {
                null
            }
        }

        if (videoUri != null) {
            Log.v("Heart uri", "$videoUri")
            rate = heartRateCalculator(videoUri, context)
        } else {
            Log.e("HeartRateVideo", "Video file not found")
        }

        return rate
    }

    // New method to calculate heart rate from a given video URI (for selected videos)
    fun readFromUri(context: Context, videoUri: Uri): Int? {
        return try {
            heartRateCalculator(videoUri, context)
        } catch (e: Exception) {
            Log.e("HeartRateReader", "Failed to calculate heart rate: ${e.message}")
            null  // Return null if there's an exception
        }
    }


    // Method to calculate heart rate from the video frames
    private fun heartRateCalculator(uri: Uri, context: Context): Int {
        val result: Int
        val retriever = MediaMetadataRetriever()
        val frameList = ArrayList<Bitmap>()
        try {
            retriever.setDataSource(context, uri)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT)
            val frameDuration = min(duration!!.toInt(), 425)
            var i = 10
            while (i < frameDuration) {
                val bitmap = retriever.getFrameAtIndex(i)
                bitmap?.let { frameList.add(it) }
                i += 15
            }
        } finally {
            retriever.release()
            var redBucket: Long
            var pixelCount: Long = 0
            val a = mutableListOf<Long>()
            for (i in frameList) {
                redBucket = 0
                for (y in 350 until 450) {
                    for (x in 350 until 450) {
                        val c: Int = i.getPixel(x, y)
                        pixelCount++
                        redBucket += Color.red(c) + Color.blue(c) + Color.green(c)
                    }
                }
                a.add(redBucket)
            }
            val b = mutableListOf<Long>()
            for (i in 0 until a.lastIndex - 5) {
                val temp = (a.elementAt(i) + a.elementAt(i + 1) + a.elementAt(i + 2)
                        + a.elementAt(i + 3) + a.elementAt(i + 4)) / 4
                b.add(temp)
            }
            var x = b.elementAt(0)
            var count = 0
            for (i in 1 until b.lastIndex) {
                val p = b.elementAt(i)
                if ((p - x) > 3500) {
                    count += 1
                }
                x = b.elementAt(i)
            }
            val rate = ((count.toFloat()) * 60).toInt()
            result = (rate / 4)
        }
        return result
    }
}
