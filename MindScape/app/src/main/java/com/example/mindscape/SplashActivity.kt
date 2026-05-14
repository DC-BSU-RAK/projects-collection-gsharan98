package com.example.mindscape

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = findViewById<ImageView>(R.id.splash_name)
        val logo = findViewById<ImageView>(R.id.splash_logo)
        val glow = findViewById<ImageView>(R.id.logo_glow)

        supportActionBar?.hide()

        // 1. ANIMATION SETUP
        logo.translationY = -1500f
        logo.alpha = 0f
        name.alpha = 0f
        glow.alpha = 0f

        // 2. PREPARE SOUNDS
        val swipeSound = MediaPlayer.create(this, R.raw.swipe_up)
        val nameSound = MediaPlayer.create(this, R.raw.name)

        // 3. LOGO DROP ANIMATION + SOUND
        logo.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator(1.5f))
            .withStartAction {
                swipeSound?.start()
            }
            .start()

        // 4. NAME FADE ANIMATION + SOUND
        // This starts after a 1 second delay (1000ms)
        name.animate()
            .alpha(1f)
            .setDuration(800)
            .setStartDelay(1000)
            .withStartAction {
                nameSound?.start() // Plays exactly when name starts appearing
            }
            .start()

        // 5. GLOW FADE
        glow.animate().alpha(0.4f).setDuration(1000).setStartDelay(1200).start()

        // Cleanup sounds when they finish to save phone memory
        swipeSound?.setOnCompletionListener { it.release() }
        nameSound?.setOnCompletionListener { it.release() }

        // 6. GOES TO MAIN ACTIVITY
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 4500)
    }
}