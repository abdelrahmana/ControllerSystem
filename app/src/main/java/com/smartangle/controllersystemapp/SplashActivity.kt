package com.smartangle.controllersystemapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.smartangle.controllersystemapp.accountant.home.AccountantHomeActivity
import com.smartangle.controllersystemapp.admin.AdminHomeActivity
import com.smartangle.controllersystemapp.callcenter.home.CallCenterHome
import com.smartangle.controllersystemapp.common.ContainerActivityForFragment
import com.smartangle.controllersystemapp.delegates.activities.DelegatesHomeActivity
import com.smartangle.util.CommonActivity
import com.smartangle.util.PrefsUtil

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
                Log.d("logged","yes")

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
                else if (PrefsUtil.getUserModel(this)?.role_id.equals("3"))
                {
                    startActivity(Intent(this , CallCenterHome::class.java))
                    finish()
                }
                else if (PrefsUtil.getUserModel(this)?.role_id.equals("4"))
                {
                    startActivity(Intent(this , DelegatesHomeActivity::class.java))
                    finish()
                }


            }
            else{
                Log.d("logged","No")

                startActivity(Intent(this , ContainerActivityForFragment::class.java))
                finish()

            }



        },4000)
    }

}