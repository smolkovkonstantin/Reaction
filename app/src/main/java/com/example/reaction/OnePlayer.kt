package com.example.reaction

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class OnePlayer : AppCompatActivity() {

    private var buttonStart: Button? = null // button of start
    private var buttonStop: Button? = null // button of stop
    private var buttonReset: Button? = null // button of reset
    private var backToMainActivity1: ImageButton? = null // button for back

    // delay before timer start
    private var randomNumber: Double = 1.0

    // user touch to button stop
    private var userTouchStop: Boolean = false

    // value when timer stops
    private val breakTime = 10000.0

    // change red color
    private val newRed = Color.RED + 70

    // change yellow color
    private val newYellow = Color.YELLOW * 40

    // set green color to stop's button
    private fun toGreen(button: Button?) {
        button?.setBackgroundColor(Color.GREEN)
    }

    // set yellow color to stop's button and name
    @SuppressLint("SetTextI18n")
    private fun toYellow(button: Button?) {
        button?.text = "WAIT!"
        button?.setBackgroundColor(newYellow)
    }

    // set yellow color to stop's button and name
    @SuppressLint("SetTextI18n")
    private fun toRed(button: Button?) {
        button?.text = "Wow!"
        button?.setBackgroundColor(newRed)
    }

    // reset visible of buttons for begin
    private fun reset() {
        buttonStart?.visibility = View.VISIBLE
        buttonStop?.visibility = View.INVISIBLE
        buttonReset?.visibility = View.INVISIBLE
    }

    // fun create delay time and start the timer
    private fun generateAndStart() {
        randomNumber = Random.nextDouble(1.0, 2.0) // генерация случайного числа

        Thread {
            while (randomNumber > 0) {
                if (userTouchStop) break
                Thread.sleep(1)
                randomNumber -= 0.001
            }
            stopwatch()
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun update(time: Int) {
        buttonStop?.text = "${time}ms"
    }

    // fun start the stopwatch
    @SuppressLint("SetTextI18n")
    private fun stopwatch() {
        var nowtime = 0.0

        Thread {
            while (nowtime < breakTime) {
                if (userTouchStop) break
                Thread.sleep(1)
                nowtime += 2.0
                runOnUiThread { update(nowtime.toInt()) }
            }
            if (nowtime == breakTime) {
                buttonStop?.text = "Hey?"
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_player)

        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)
        buttonReset = findViewById(R.id.buttonReset)
        backToMainActivity1 = findViewById(R.id.back_to_MainActivity)

        buttonStop?.visibility = View.INVISIBLE
        buttonReset?.visibility = View.INVISIBLE

        buttonStart?.setOnClickListener {
            buttonStart?.visibility = View.INVISIBLE
            buttonStop?.visibility = View.VISIBLE
            buttonStop?.isClickable = true

            userTouchStop = false
            toYellow(buttonStop)

            generateAndStart()
        }

        buttonStop?.setOnClickListener {
            userTouchStop = true

            if (randomNumber > 0) {
                toRed(buttonStop)
                buttonStop?.isClickable = false
            } else {
                if (buttonStop?.text != "Hey?") {
                    toGreen(buttonStop)
                    buttonStop?.isClickable = false
                }
            }

            buttonReset?.visibility = View.VISIBLE
        }

        buttonReset?.setOnClickListener {
            reset()
        }

        backToMainActivity1?.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.right_out, R.anim.left_in)
            finish()
        }
    }

    // redefining the animation for the back button into the device
    @Override
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.right_out, R.anim.left_in)
        finish()
    }
}