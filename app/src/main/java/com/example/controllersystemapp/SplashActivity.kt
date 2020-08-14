package com.example.controllersystemapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.controllersystemapp.common.ContainerActivityForFragment
import com.example.util.CommonActivity

class SplashActivity : CommonActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handleSplashWait()

    }


    private fun handleSplashWait() {
        Handler().postDelayed({

            startActivity(Intent(this , ContainerActivityForFragment::class.java))

        },4000)
    }

}