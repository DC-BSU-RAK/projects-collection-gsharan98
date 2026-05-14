package com.example.mindscape

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ActivitySelection : AppCompatActivity() {

    private fun playSound(resId: Int) {
        val effectPlayer = MediaPlayer.create(this, resId)
        effectPlayer.setOnCompletionListener { it.release() }
        effectPlayer.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_selection)

        val btnInfo = findViewById<ImageView>(R.id.info_btn)

        // Activity Buttons
        val activityButtons = listOf(
            findViewById<ImageView>(R.id.btn_learning) to "Learning",
            findViewById<ImageView>(R.id.btn_meditating) to "Meditating",
            findViewById<ImageView>(R.id.btn_writing) to "Writing",
            findViewById<ImageView>(R.id.btn_memorizing) to "Memorizing",
            findViewById<ImageView>(R.id.btn_exercising) to "Exercising"
        )

        activityButtons.forEach { (button, name) ->
            button.setOnClickListener {
                playSound(R.raw.click)
                val sharedPref = getSharedPreferences("MindScapePrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("SELECTED_ACTIVITY", name).apply()
                startActivity(Intent(this, ThemeSelection::class.java))
            }
        }

        btnInfo.setOnClickListener {
            playSound(R.raw.swipe_up)
            showPopup()
        }
    }

    private fun showPopup() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_info_sheet, null)
        dialog.setContentView(view)
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            it.setBackgroundResource(android.R.color.transparent)
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    override fun onResume() { super.onResume(); MusicManager.startMusic(this) }
    override fun onPause() { super.onPause() }
}