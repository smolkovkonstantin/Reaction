package com.example.reaction

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class OneVsOne : AppCompatActivity(), View.OnTouchListener {

    // delay before timer start
    private var randomNumber: Double = 1.0

    // someone touch
    private var someoneTouch: Boolean = false

    // left user touch to button stop
    private var leftUser: Boolean = false

    // right user touch to button stop
    private var rightUser: Boolean = false

    // value when timer stops
    private val breakTime = 10000.0

    private var backToMainActivity2: ImageButton? = null // button for back

    // left player
    private var buttonLeftPlayer: Button? = null

    // right player
    private var buttonRightPlayer: Button? = null

    // button of reset
    private var buttonReset: Button? = null

    // set green color to stop's button
    private fun toGreen(button: Button?) {
        button?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FF00FF00"))
    }

    // set yellow color to stop's button and name
    private fun toYellow(button: Button?) {
        button?.text = resources.getString(R.string.wait)
        button?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFD800"))
    }

    // set yellow color to stop's button and name
    private fun toRed(button: Button?) {
        button?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFF0046"))
    }

    // fun create delay time and start the timer
    private fun generateAndStart() {
        randomNumber = Random.nextDouble(1.0, 2.0) // генерация случайного числа

        Thread {
            while (randomNumber > 0) {
                if (someoneTouch) break
                Thread.sleep(1)
                randomNumber -= 0.001
            }
            if (!someoneTouch) {
                stopwatchLeft()
                stopwatchRight()
            }
        }.start()
    }

    // fun start the stopwatch
    private fun stopwatchLeft() {
        var nowTimeLeft = 0.0

        Thread {
            while (nowTimeLeft < breakTime) {
                if (leftUser) break
                Thread.sleep(1)
                nowTimeLeft += 2.0
                runOnUiThread { update(nowTimeLeft.toInt(), buttonLeftPlayer) }
            }
            if (nowTimeLeft == breakTime) {
                buttonLeftPlayer?.text = resources.getString(R.string.hey)
            }
        }.start()
    }

    private fun stopwatchRight() {
        var nowTimeRight = 0.0

        Thread {
            while (nowTimeRight < breakTime) {
                if (rightUser) break
                Thread.sleep(1)
                nowTimeRight += 2.0
                runOnUiThread { update(nowTimeRight.toInt(), buttonRightPlayer) }
            }
            if (nowTimeRight == breakTime) {
                buttonRightPlayer?.text = resources.getString(R.string.hey)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun update(time: Int, button: Button?) {
        button?.text = "${time}ms"
    }

    private fun doIfStart(buttonLeft: Button?, buttonRight: Button?) {
        toYellow(buttonLeft)
        toYellow(buttonRight)

        generateAndStart()
    }

    private fun doIfWait(loser: Button?, winner: Button?) {
        someoneTouch = true

        toRed(loser)
        loser?.text = resources.getString(R.string.wow)
        loser?.isClickable = false

        toGreen(winner)
        winner?.text = resources.getString(R.string.win)
        winner?.isClickable = false

        buttonReset?.visibility = View.VISIBLE
    }

    private fun doIfSomeWin(loser: Button?, winner: Button?) {

        toRed(loser)

        toGreen(winner)

        buttonReset?.visibility = View.VISIBLE
    }

    @SuppressLint("ResourceAsColor")
    private fun reset(button: Button?) {
        button?.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFA100EC"))
        button?.text = resources.getString(R.string.start)
        button?.isClickable = true

        someoneTouch = false
        leftUser = false
        rightUser = false

        buttonReset?.visibility = View.INVISIBLE
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.right_in, R.anim.left_out)
        finish()
    }

    @SuppressLint("ResourceAsColor", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_vs_one)

        // describe the function of the button to return to the main
        backToMainActivity2 = findViewById(R.id.back_to_MainActivity2)
        // left player
        buttonLeftPlayer = findViewById(R.id.buttonLeft)
        // right player
        buttonRightPlayer = findViewById(R.id.buttonRight)

        // reset set INVISIBLE
        buttonReset = findViewById(R.id.buttonReset2)
        buttonReset?.visibility = View.INVISIBLE

        arrayOf(buttonReset, buttonRightPlayer, buttonLeftPlayer, backToMainActivity2).forEach {
            it?.setOnTouchListener(this)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        backToMain()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        when (p0) {
            buttonLeftPlayer -> {
                if (p1?.action == MotionEvent.ACTION_UP) {
                    when (buttonLeftPlayer?.text) {
                        resources.getString(R.string.start) ->
                            doIfStart(buttonLeftPlayer, buttonRightPlayer)
                        resources.getString(R.string.wait) ->
                            doIfWait(buttonLeftPlayer, buttonRightPlayer)
                        resources.getString(R.string.hey) -> reset(buttonLeftPlayer)
                        resources.getString(R.string.wow) -> {}
                        resources.getString(R.string.win) -> {}
                        else -> {
                            buttonLeftPlayer?.isClickable = false
                            if (rightUser && !leftUser) {
                                doIfSomeWin(buttonLeftPlayer, buttonRightPlayer)
                            }
                            leftUser = true
                        }
                    }
                }
            }
            buttonRightPlayer -> {
                if (p1?.action == MotionEvent.ACTION_UP) {
                    when (buttonRightPlayer?.text) {
                        resources.getString(R.string.start) ->
                            doIfStart(buttonRightPlayer, buttonLeftPlayer)
                        resources.getString(R.string.wait) ->
                            doIfWait(buttonRightPlayer, buttonLeftPlayer)
                        resources.getString(R.string.hey) -> reset(buttonRightPlayer)
                        resources.getString(R.string.wow) -> {}
                        resources.getString(R.string.win) -> {}
                        else -> {
                            buttonRightPlayer?.isClickable = false
                            if (leftUser && !rightUser) {
                                doIfSomeWin(buttonRightPlayer, buttonLeftPlayer)
                            }
                            rightUser = true
                        }
                    }
                }
            }
            buttonReset -> {
                if (p1?.action == MotionEvent.ACTION_UP) {
                    reset(buttonLeftPlayer)
                    reset(buttonRightPlayer)
                }
            }
            backToMainActivity2 -> {
                backToMain()
            }
        }
        return true
    }
}