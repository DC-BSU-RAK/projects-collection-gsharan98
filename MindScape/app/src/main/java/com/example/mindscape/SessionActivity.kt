package com.example.mindscape

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class SessionActivity : AppCompatActivity() {

    private var seconds = 0
    private var isRunning = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerText: TextView
    private lateinit var motivationText: TextView
    private var currentActivityName: String = "Focus"
    private var currentThemeName: String = "Calm"

    private val quotes = listOf(
        "Energy flows where attention goes.",
        "Take a deep breath and center yourself.",
        "Quality is not an act, it is a habit.",
        "Your mind is your most powerful tool.",
        "Small steps lead to big results.",
        "Stay present, stay focused."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_session)

        // Initialize Views
        timerText = findViewById(R.id.timer_text)
        motivationText = findViewById(R.id.motivation_text)
        val btnToggle = findViewById<ImageView>(R.id.btn_toggle)
        val btnEnd = findViewById<ImageView>(R.id.btn_end)
        val displayActivity = findViewById<TextView>(R.id.display_activity)

        val sharedPref = getSharedPreferences("MindScapePrefs", Context.MODE_PRIVATE)

        val isResumed = intent.getBooleanExtra("IS_RESUMED", false)

        if (isResumed) {
            seconds = sharedPref.getInt("SAVED_SECONDS", 0)
            currentActivityName = sharedPref.getString("SAVED_ACTIVITY", "Focus") ?: "Focus"
            currentThemeName = sharedPref.getString("SAVED_THEME", "Calm") ?: "Calm"
        } else {
            currentActivityName = sharedPref.getString("SELECTED_ACTIVITY", "Focus") ?: "Focus"
            currentThemeName = sharedPref.getString("SELECTED_THEME", "Calm") ?: "Calm"
        }

        displayActivity.text = currentActivityName.uppercase()

        if (isRunning) {
            btnToggle.setImageResource(R.drawable.pause)
        } else {
            btnToggle.setImageResource(R.drawable.resume_bt)
        }

        runTimer()
        playThemeMusic()

        btnToggle.setOnClickListener {
            if (isRunning) {
                isRunning = false
                MusicManager.pauseMusic()
                btnToggle.setImageResource(R.drawable.resume_bt)
                motivationText.text = "Session Paused"
            } else {
                isRunning = true
                playThemeMusic()
                btnToggle.setImageResource(R.drawable.pause)
                motivationText.text = quotes.random()
            }
        }

        btnEnd.setOnClickListener {
            showSavePopup()
        }
    }

    private fun runTimer() {
        handler.removeCallbacksAndMessages(null)
        handler.post(object : Runnable {
            override fun run() {
                if (isRunning) {
                    seconds++
                    val mins = (seconds % 3600) / 60
                    val secs = seconds % 60
                    timerText.text = String.format("%02d:%02d", mins, secs)

                    if (seconds % 30 == 0) {
                        motivationText.text = quotes.random()
                    }
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun showSavePopup() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_save_sheet, null)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnPopSave = view.findViewById<ImageView>(R.id.btn_pop_save)
        val btnPopEnd = view.findViewById<ImageView>(R.id.btn_pop_end)

        btnPopSave.setOnClickListener {
            val sharedPref = getSharedPreferences("MindScapePrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("SAVED_SECONDS", seconds)
                putString("SAVED_ACTIVITY", currentActivityName)
                putString("SAVED_THEME", currentThemeName)
                putBoolean("HAS_SAVED_SESSION", true)
                apply()
            }
            dialog.dismiss()
            goHome()
        }

        btnPopEnd.setOnClickListener {
            dialog.dismiss()
            goHome()
        }
        dialog.show()
    }

    private fun goHome() {
        MusicManager.startMusic(this, R.raw.background)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun playThemeMusic() {
        val track = when (currentThemeName) {
            "Deep Focus" -> R.raw.deep_focus
            "Calm" -> R.raw.calm
            "Lofi" -> R.raw.lofi
            else -> R.raw.background
        }
        MusicManager.startMusic(this, track)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}