package com.example.reaction

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null // кпонка начала
    private var buttonStop: Button? = null // кпонка конца
    private var buttonReset: Button? = null // кпонка конца

    // задержка перед стартом таймера
    private var randomNumber: Double = 1.0

    // пользователь дотронулся до кнопки остановки
    private var userTouchStop: Boolean = false

    // флаг показывающий, что можно начать отсчёт
    private var timeStart: Boolean = false

    // значение при котором таймер останавливается
    private val breakTime = 600000.0


    private fun toGreen() {
        buttonStop?.setBackgroundColor(Color.GREEN)
    }

    private fun toYellow() {
        buttonStop?.setBackgroundColor(Color.YELLOW)
    }

    private fun toRed() {
        buttonStop?.setBackgroundColor(Color.RED)
    }

    private fun reset() {
        buttonStart?.visibility = View.VISIBLE
        buttonStop?.visibility = View.INVISIBLE
        buttonReset?.visibility = View.INVISIBLE
    }

    // функция определяющая задержку перед стартом секундомера
    private fun generateAndStart() {
        randomNumber = Random.nextDouble(1.0, 2.0) // генерация случайного числа

        val thread = Thread {
            while (randomNumber > 0) {
                Thread.sleep(1)
                randomNumber -= 0.001
            }
            timeStart = true
            stopwatch()
        }
        thread.start()
    }

    @SuppressLint("SetTextI18n")
    private fun update(time: Int) {
        buttonStop?.text = "${time}ms"
    }

    private fun stopwatch() {
        var nowtime = 0.0
        var currentTime: Int

        Thread {
            while (timeStart && nowtime < breakTime) {
                if (userTouchStop) break
                Thread.sleep(1)
                nowtime += 2.0
                currentTime = nowtime.toInt()
                update(currentTime)
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

            buttonStop?.text = "WAIT!"
            userTouchStop = false
            toYellow()

            buttonStop?.setOnClickListener {
                userTouchStop = true
                if (randomNumber > 0) {
                    buttonStop?.text = "Wow!"
                    toRed()
                } else if (buttonStop?.text != "Wow!") toGreen()
                else if (buttonStop?.text == "Wow!") toRed()
                else if (buttonStop?.text.toString().toDouble() == breakTime) reset()
                buttonReset?.visibility = View.VISIBLE
            }

            generateAndStart()
        }

        buttonReset?.setOnClickListener {
            reset()
        }
    }
}
