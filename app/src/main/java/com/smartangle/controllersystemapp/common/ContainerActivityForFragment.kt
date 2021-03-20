package com.smartangle.controllersystemapp.common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.smartangle.controllersystemapp.common.chat.ChatFragment
import com.smartangle.controllersystemapp.common.login.LoginFragment
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin

// this actiivity is used to add fragments inside it
class ContainerActivityForFragment : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container_for_fragment)
       // when(intent.getStringExtra(whichFragment,))
             //UtilKotlin.changeFragment(AddProductFragment(),supportFragmentManager,R.id.container)
       // UtilKotlin.changeFragment(LoginFragment(),supportFragmentManager,R.id.container)

        if (intent?.getBooleanExtra(isThatForChat,false) == true) // default is false
            UtilKotlin.changeFragmentBack(this , ChatFragment() , "chat_fragment"  , Bundle().also {
                it.putString(DelegatesFragment.SELECTEDDELEGATE,intent?.getStringExtra(NameUtils.other_info))
            } , R.id.container)
else
        UtilKotlin.changeFragmentBack(this , LoginFragment() , "LoginFragment"  , null , R.id.container)

        //  UtilKotlin.changeFragment(RequestOfferProfileFragment(),supportFragmentManager,R.id.container)
       // StatusBarUtil.setTransparent(this)

    }
    companion object {
        val whichFragment = "current_fragment"
        val isThatForChat = "is_for_chat"
    }


    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is ChatFragment)
          finish()
        else {

            if (count > 0) {
                Log.d("count", "first $count")

                if (count == 1) {
//                Log.d("count","finish $count")
                    val a =
                        Intent(Intent.ACTION_MAIN)
                    a.addCategory(Intent.CATEGORY_HOME)
                    a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(a)
                } else {
                    supportFragmentManager.popBackStack()
                    //return
                }
            } else {
                super.onBackPressed()
            }

        }
    }
}
