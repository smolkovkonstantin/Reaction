package com.example.reaction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button

class MainActivity : Activity(), View.OnTouchListener {

    var buttonOneVsOne: Button? = null
    var buttonOnePlayer: Button? = null


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonOneVsOne = findViewById(R.id.one_vs_one)
        buttonOnePlayer = findViewById(R.id.one_player)

        buttonOneVsOne?.setOnTouchListener(this)
        buttonOnePlayer?.setOnTouchListener(this)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

        when (p0) {
            buttonOnePlayer -> {
                if (p1?.action == MotionEvent.ACTION_UP) {
                    val intent = Intent(this, OnePlayer::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_in, R.anim.left_out)
                    finish()
                }
            }
            buttonOneVsOne -> {
                if (p1?.action == MotionEvent.ACTION_UP) {
                    val intent = Intent(this, OneVsOne::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.right_out, R.anim.left_in)
                    finish()
                }
            }
        }

        return true
    }
}

