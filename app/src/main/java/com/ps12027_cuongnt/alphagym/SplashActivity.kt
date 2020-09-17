package com.ps12027_cuongnt.alphagym

import android.content.Intent

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed(Runnable {
            val mySuperIntent = Intent(this, MainActivity::class.java)
            startActivity(mySuperIntent)

            finish()
        }, 3000)
    }
}

