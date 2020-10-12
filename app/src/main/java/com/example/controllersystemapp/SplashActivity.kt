package com.example.controllersystemapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.controllersystemapp.accountant.home.AccountantHomeActivity
import com.example.controllersystemapp.admin.AdminHomeActivity
import com.example.controllersystemapp.common.ContainerActivityForFragment
import com.example.util.CommonActivity
import com.example.util.PrefsUtil

class SplashActivity : CommonActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handleSplashWait()

    }


    private fun handleSplashWait() {
        Handler().postDelayed({

            if (PrefsUtil.isLoggedIn(this))
            {
                if (PrefsUtil.getUserModel(this)?.role_id.equals("1"))
                {
                    startActivity(Intent(this , AdminHomeActivity::class.java))
                    finish()
                }
                else if (PrefsUtil.getUserModel(this)?.role_id.equals("2"))
                {
                    startActivity(Intent(this , AccountantHomeActivity::class.java))
                    finish()
                }


            }
            else{
                startActivity(Intent(this , ContainerActivityForFragment::class.java))
                finish()

            }



        },4000)
    }

}