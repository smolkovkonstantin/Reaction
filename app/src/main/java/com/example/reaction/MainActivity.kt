package com.example.reaction

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ThemedSpinnerAdapter
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var buttonStart: Button? = null // кпонка начала
    private var buttonStop: Button? = null // кпонка конца
    private var buttonReset: Button? = null // кпонка конца

    // флаг показывающий нажата ли кпонка начала прежде, чем кнопка конца
    private var flag: Boolean = false

    private fun toGreen(button: Button?) {
        button?.setBackgroundColor(Color.GREEN)
    }

    private fun generateAndStart() {
        var randomNumber = Random.nextDouble(0.5, 1.0) // генерация случайного числа

        val thread = Thread {
            while (randomNumber > 0) {
                Thread.sleep(1)
                randomNumber -= 0.001
            }
            runOnUiThread { buttonStop?.visibility = View.VISIBLE }
        }

        thread.start()

        flag = false

        while (thread.isAlive)
            flag = true

    }

    @SuppressLint("SetTextI18n")
    private fun update(button: Button?, time: Int) {
        button?.text = "${time}ms".lowercase()
    }

    private fun stopwatch(button: Button?) {
        flag = true
        var nowtime = 0

        Thread {
            while (flag && nowtime < 600000) {
                Thread.sleep(1)
                nowtime += 1
                update(button, nowtime)
                button?.setOnClickListener {
                    flag = false
                }
            }
            runOnUiThread { buttonReset?.visibility = View.VISIBLE }
        }.start()
    }


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

            generateAndStart()

            toGreen(buttonStop)

            stopwatch(buttonStop)
        }

        buttonReset?.setOnClickListener {
            buttonStart?.visibility = View.VISIBLE
            buttonStop?.visibility = View.INVISIBLE
            buttonReset?.visibility = View.INVISIBLE
        }
    }
}
