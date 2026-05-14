package com.example.getglazed

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var leftItem: ImageView
    lateinit var rightItem: ImageView

    lateinit var resultPopup: FrameLayout
    lateinit var resultImage: ImageView
    lateinit var resultTitle: TextView
    lateinit var resultDesc: TextView

    lateinit var strawberry: ImageView
    lateinit var chocolate: ImageView
    lateinit var caramel: ImageView
    lateinit var sugar: ImageView

    // INFO (ADDED ONLY)
    lateinit var info: ImageView
    lateinit var infoPopup: FrameLayout
    lateinit var infoImage: ImageView

    var firstChoice: String? = null
    var secondChoice: String? = null

    private val handler = Handler(Looper.getMainLooper())

    private var bgMusic: MediaPlayer? = null
    private var clickSound: MediaPlayer? = null
    private var resultSound: MediaPlayer? = null

    //  INFO SOUND (ADDED ONLY)
    private var infoSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        leftItem = findViewById(R.id.leftItem)
        rightItem = findViewById(R.id.rightItem)

        resultPopup = findViewById(R.id.resultPopup)
        resultImage = findViewById(R.id.resultImage)
        resultTitle = findViewById(R.id.resultTitle)
        resultDesc = findViewById(R.id.resultDesc)

        strawberry = findViewById(R.id.strawberry)
        chocolate = findViewById(R.id.chocolate)
        caramel = findViewById(R.id.caramel)
        sugar = findViewById(R.id.sugar)

        //  INFO INIT (ADDED ONLY)
        info = findViewById(R.id.info)
        infoPopup = findViewById(R.id.infoPopup)
        infoImage = findViewById(R.id.infoImage)

        //  MUSIC
        bgMusic = MediaPlayer.create(this, R.raw.bg_music)
        bgMusic?.isLooping = true
        bgMusic?.start()

        clickSound = MediaPlayer.create(this, R.raw.click)
        resultSound = MediaPlayer.create(this, R.raw.result)

        // INFO SOUND INIT (ADDED ONLY)
        infoSound = MediaPlayer.create(this, R.raw.click)

        startFloating()

        strawberry.setOnClickListener { handleClick("strawberry", R.drawable.strawberry) }
        chocolate.setOnClickListener { handleClick("chocolate", R.drawable.chocolate) }
        caramel.setOnClickListener { handleClick("caramel", R.drawable.caramel) }
        sugar.setOnClickListener { handleClick("sugar", R.drawable.sugar) }

        resultPopup.setOnClickListener { resetAll() }

        // INFO CLICK (WITH SOUND)
        info.setOnClickListener {
            infoPopup.visibility = View.VISIBLE

            infoSound?.let {
                if (it.isPlaying) it.seekTo(0)
                it.start()
            }
        }

        // CLOSE POPUP
        infoPopup.setOnClickListener {
            infoPopup.visibility = View.GONE
        }

        infoImage.setOnClickListener {
            // do nothing
        }
    }

    // ---------------- CLICK ----------------
    private fun handleClick(name: String, imageRes: Int) {

        clickSound?.let {
            if (it.isPlaying) it.seekTo(0)
            it.start()
        }

        when {
            firstChoice == null -> {
                firstChoice = name
                leftItem.setImageResource(imageRes)
            }

            secondChoice == null -> {
                secondChoice = name
                rightItem.setImageResource(imageRes)
                showResult()
            }

            else -> {
                resetAll()
                firstChoice = name
                leftItem.setImageResource(imageRes)
            }
        }
    }

    // ---------------- RESULT ----------------
    private fun showResult() {

        val combo1 = "${firstChoice}_${secondChoice}"
        val combo2 = "${secondChoice}_${firstChoice}"

        var title = ""
        var desc = ""
        var imageRes: Int? = null

        when {
            combo1 == "strawberry_sugar" || combo2 == "strawberry_sugar" -> {
                title = "Strawberry Sugar Donut"
                desc = "Soft strawberry donut topped with sugar crystals."
                imageRes = R.drawable.strawberry_sugar
            }

            combo1 == "chocolate_sugar" || combo2 == "chocolate_sugar" -> {
                title = "Chocolate Sugar Donut"
                desc = "Rich chocolate donut with sugar finish."
                imageRes = R.drawable.chocolate_sugar
            }

            combo1 == "strawberry_chocolate" || combo2 == "strawberry_chocolate" -> {
                title = "Strawberry Chocolate Donut"
                desc = "Fruity strawberry with rich chocolate."
                imageRes = R.drawable.strawberry_chocolate
            }

            combo1 == "caramel_chocolate" || combo2 == "caramel_chocolate" -> {
                title = "Caramel Chocolate Donut"
                desc = "Smooth caramel blended with chocolate."
                imageRes = R.drawable.caramel_chocolate
            }

            combo1 == "caramel_strawberry" || combo2 == "caramel_strawberry" -> {
                title = "Caramel Strawberry Donut"
                desc = "Sweet caramel meets fresh strawberry."
                imageRes = R.drawable.caramel_strawberry
            }

            combo1 == "caramel_sugar" || combo2 == "caramel_sugar" -> {
                title = "Caramel Sugar Donut"
                desc = "Classic caramel sugar donut."
                imageRes = R.drawable.caramel_sugar
            }
        }

        if (imageRes != null) {

            resultImage.setImageResource(imageRes)
            resultTitle.text = title
            resultDesc.text = desc

            handler.postDelayed({

                resultPopup.visibility = View.VISIBLE

                resultPopup.alpha = 0f
                resultPopup.scaleX = 0.95f
                resultPopup.scaleY = 0.95f

                resultPopup.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .start()

                resultSound?.let {
                    if (it.isPlaying) it.seekTo(0)
                    it.start()
                }

            }, 500)
        }
    }

    // ---------------- FLOATING ----------------
    private fun startFloating() {
        floatView(strawberry, 25f, 0)
        floatView(chocolate, -30f, 200)
        floatView(caramel, 20f, 400)
        floatView(sugar, -25f, 600)
    }

    private fun floatView(view: View, distance: Float, delay: Long) {
        handler.postDelayed(object : Runnable {
            override fun run() {

                view.animate()
                    .translationY(distance)
                    .setDuration(1500)
                    .setInterpolator(LinearInterpolator())
                    .withEndAction {
                        view.animate()
                            .translationY(0f)
                            .setDuration(1500)
                            .setInterpolator(LinearInterpolator())
                            .withEndAction {
                                handler.post(this)
                            }
                    }
                    .start()
            }
        }, delay)
    }

    // ---------------- RESET ----------------
    private fun resetAll() {

        firstChoice = null
        secondChoice = null

        leftItem.setImageDrawable(null)
        rightItem.setImageDrawable(null)

        resultPopup.visibility = View.GONE

        startFloating()
    }

    // ---------------- CLEANUP ----------------
    override fun onDestroy() {
        super.onDestroy()

        bgMusic?.release()
        clickSound?.release()
        resultSound?.release()
        infoSound?.release()

        bgMusic = null
        clickSound = null
        resultSound = null
        infoSound = null
    }
}