package com.example.getglazed

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val sloganText = "Make your perfect donut "
    private var index = 0
    private val delay: Long = 50 // typing speed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)

        val logo = findViewById<ImageView>(R.id.logo)
        val slogan = findViewById<TextView>(R.id.slogan)
        val brand = findViewById<TextView>(R.id.brand) // new TextView for GET GLAZED

        // ---------- Donut bounce animation ----------
        val bounceAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        logo.startAnimation(bounceAnim)

        // ---------- Typing effect ----------
        fun typeWriter() {
            if (index <= sloganText.length) {
                slogan.text = sloganText.substring(0, index)
                index++
                Handler(Looper.getMainLooper()).postDelayed({ typeWriter() }, delay)
            } else {
                // When first line is done, show "GET GLAZED"
                brand.text = "-GET GLAZED!"
            }
        }
        typeWriter()

        // ---------- Move to MainActivity after 3.5 seconds ----------
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3500)
    }
}