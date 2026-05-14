package com.example.mindscape

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class CountdownActivity : AppCompatActivity() {

    private var timerPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_countdown)

        val countdownText = findViewById<TextView>(R.id.countdown_text)

        // Play timer sound
        timerPlayer = MediaPlayer.create(this, R.raw.timer)
        timerPlayer?.start()

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = (millisUntilFinished / 1000) + 1
                countdownText.text = secondsRemaining.toString()
            }

            override fun onFinish() {
                timerPlayer?.stop()
                timerPlayer?.release()
                timerPlayer = null

                startActivity(Intent(this@CountdownActivity, SessionActivity::class.java))
                finish()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerPlayer?.release()
    }
}