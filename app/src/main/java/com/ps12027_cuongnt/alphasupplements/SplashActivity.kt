package com.ps12027_cuongnt.alphasupplements

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    var clLogo: ConstraintLayout? = null
    var splashProgress: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashProgress = findViewById(R.id.splashProgress)
        clLogo = findViewById(R.id.clLogo)
        /*val animation =
            AnimationUtils.loadAnimation(this, R.anim.logoscroll)
        clLogo?.startAnimation(animation)*/

        Handler().postDelayed(Runnable {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }, 1000)


    }
}

