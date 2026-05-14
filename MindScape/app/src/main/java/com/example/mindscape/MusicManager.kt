package com.example.mindscape

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentResId: Int = -1

    fun startMusic(context: Context, resId: Int = R.raw.background) {
        // If the requested song is already playing, do nothing
        if (currentResId == resId && mediaPlayer?.isPlaying == true) return

        // Stop current music before starting new track
        stopMusic()

        try {
            mediaPlayer = MediaPlayer.create(context.applicationContext, resId)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            currentResId = resId
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentResId = -1
    }
}