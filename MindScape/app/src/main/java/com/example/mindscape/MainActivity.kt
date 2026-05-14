package com.example.mindscape

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Start background music
        MusicManager.startMusic(this, R.raw.background)

        val btnPrevious = findViewById<ImageView>(R.id.home_bt_one) // Top Button
        val btnStart = findViewById<ImageView>(R.id.home_bt_two)    // Bottom Button

        // 1. TOP BUTTON: RESUME PREVIOUS SESSION
        btnPrevious.setOnClickListener {
            playSound(R.raw.click)
            val sharedPref = getSharedPreferences("MindScapePrefs", Context.MODE_PRIVATE)
            val hasSave = sharedPref.getBoolean("HAS_SAVED_SESSION", false)

            if (hasSave) {
                val intent = Intent(this, SessionActivity::class.java)
                intent.putExtra("IS_RESUMED", true)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No previous session found", Toast.LENGTH_SHORT).show()
            }
        }

        // 2. BOTTOM BUTTON: START NEW SESSION
        btnStart.setOnClickListener {
            playSound(R.raw.click)
            val intent = Intent(this, ActivitySelection::class.java)
            startActivity(intent)
        }
    }

    private fun playSound(resId: Int) {
        val mediaPlayer = MediaPlayer.create(this, resId)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }

    override fun onResume() {
        super.onResume()
        MusicManager.startMusic(this, R.raw.background)
    }
}