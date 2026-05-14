package com.example.mindscape

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Make sure this matches your XML filename exactly!
        setContentView(R.layout.layout_info_sheet)

        // This handles the back button on your info page
        val backBtn = findViewById<ImageView>(R.id.back_btn)
        backBtn.setOnClickListener {
            finish()
        }
    }
}