package com.ps12027_cuongnt.alphagym

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    var clLogo: ConstraintLayout? = null
    var splashProgress: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashProgress = findViewById(R.id.splashProgress)
        clLogo = findViewById(R.id.clLogo)
        val animation =
            AnimationUtils.loadAnimation(this, R.anim.logoscroll)
        clLogo?.startAnimation(animation)

        val time: Long = 5000
        Handler().postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, time)


    }
}

