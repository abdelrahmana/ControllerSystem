package com.example.controllersystemapp.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.AddProductFragment
import com.example.controllersystemapp.common.forgetpassword.ForgetPassword
import com.example.controllersystemapp.common.login.LoginFragment
import com.example.util.CommonActivity
import com.example.util.UtilKotlin
import com.jaeger.library.StatusBarUtil

// this actiivity is used to add fragments inside it
class ContainerActivityForFragment : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container_for_fragment)
       // when(intent.getStringExtra(whichFragment,))
             //UtilKotlin.changeFragment(AddProductFragment(),supportFragmentManager,R.id.container)
       // UtilKotlin.changeFragment(LoginFragment(),supportFragmentManager,R.id.container)
        UtilKotlin.changeFragmentBack(this , LoginFragment() , "LoginFragment"  , null , R.id.container)

        //  UtilKotlin.changeFragment(RequestOfferProfileFragment(),supportFragmentManager,R.id.container)
       // StatusBarUtil.setTransparent(this)


    }
    companion object {
        val whichFragment = "current_fragment"
    }


    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count > 0) {
            Log.d("count","first $count")

            if (count == 1)
            {
//                Log.d("count","finish $count")
                val a =
                    Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
            else{
                supportFragmentManager.popBackStack()
                //return
            }
        } else {
            super.onBackPressed()
        }

    }
}
