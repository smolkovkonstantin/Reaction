package com.example.reaction

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null // кпонка начала
    private var buttonStop: Button? = null // кпонка конца
    private var buttonReset: Button? = null // кпонка конца

    // delay before timer start
    private var randomNumber: Double = 1.0

    // user touch to button stop
    private var userTouchStop: Boolean = false

    // flag that show can start counting
    private var timeStart: Boolean = false

    // value when timer stops
    private val breakTime = 10000.0

    // change red color
    private val newRed = Color.RED + 70

    // change yellow color
    private val newYellow = Color.YELLOW * 40

    // set green color to stop's button
    private fun toGreen() {
        buttonStop?.setBackgroundColor(Color.GREEN)
    }

    // set yellow color to stop's button and name
    @SuppressLint("SetTextI18n")
    private fun toYellow() {
        buttonStop?.text = "WAIT!"
        buttonStop?.setBackgroundColor(newYellow)
    }

    // set yellow color to stop's button and name
    @SuppressLint("SetTextI18n")
    private fun toRed() {
        buttonStop?.text = "Wow!"
        buttonStop?.setBackgroundColor(newRed)
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
            timeStart = true
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
            while (timeStart && nowtime < breakTime) {
                if (userTouchStop) break
                Thread.sleep(1)
                nowtime += 2.0
                update(nowtime.toInt())
            }
            if (nowtime == breakTime) {
                buttonStop?.text = "Hey?"
            }
        }.start()
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart = findViewById(R.id.buttonStart)
        buttonStop = findViewById(R.id.buttonStop)
        buttonReset = findViewById(R.id.buttonReset)

        buttonStop?.visibility = View.INVISIBLE
        buttonReset?.visibility = View.INVISIBLE

        buttonStart?.setOnClickListener {
            buttonStart?.visibility = View.INVISIBLE
            buttonStop?.visibility = View.VISIBLE
            buttonStop?.isClickable = true

            userTouchStop = false
            toYellow()

            generateAndStart()
        }

        buttonStop?.setOnClickListener {
            userTouchStop = true

            if (randomNumber > 0) {
                toRed()
                buttonStop?.isClickable = false
            } else {
                if (buttonStop?.text != "Hey?") {
                    toGreen()
                }
            }

            buttonReset?.visibility = View.VISIBLE
        }

        buttonReset?.setOnClickListener {
            reset()
        }
    }
}
