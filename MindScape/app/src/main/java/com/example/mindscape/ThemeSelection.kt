package com.example.mindscape

import android.content.Context
import android.content.Intent // Added this import
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

class ThemeSelection : AppCompatActivity() {

    private fun playSound(resId: Int) {
        val effectPlayer = MediaPlayer.create(this, resId)
        effectPlayer.setOnCompletionListener { it.release() }
        effectPlayer.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_theme_selection)

        val btnInfo = findViewById<ImageView>(R.id.info_btn)
        val btnBack = findViewById<ImageView>(R.id.back_btn)

        findViewById<ImageView>(R.id.btn_deep_focus).setOnClickListener { saveAndProceed("Deep Focus") }
        findViewById<ImageView>(R.id.btn_calm).setOnClickListener { saveAndProceed("Calm") }
        findViewById<ImageView>(R.id.btn_lofi).setOnClickListener { saveAndProceed("Lofi") }

        btnBack.setOnClickListener {
            playSound(R.raw.click)
            finish()
        }

        btnInfo.setOnClickListener {
            playSound(R.raw.swipe_up)
            showPopup()
        }
    }

    private fun saveAndProceed(themeName: String) {
        playSound(R.raw.click)

        // 1. Save the Theme Choice Permanently
        val sharedPref = getSharedPreferences("MindScapePrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("SELECTED_THEME", themeName).apply()

        // 2. Launch the Countdown Bridge
        val intent = Intent(this, CountdownActivity::class.java)
        startActivity(intent)

        //  finish() this activity so user can't go back to selection once countdown starts
        // finish()
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

    override fun onResume() {
        super.onResume()
        MusicManager.startMusic(this)
    }

    override fun onPause() {
        super.onPause()
        // Keeping it empty so music overlaps/continues during transition
    }
}