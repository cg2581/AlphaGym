package com.ps12027_cuongnt.alphagym

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ps12027_cuongnt.alphagym.Fragment.LoginFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginFragment = LoginFragment()
        val frm = supportFragmentManager
        frm.beginTransaction()
            .replace(R.id.frameLayout, loginFragment)
            .commit()
    }
}