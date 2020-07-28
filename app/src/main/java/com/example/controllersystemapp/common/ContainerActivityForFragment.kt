package com.example.controllersystemapp.common

import android.os.Bundle
import com.example.controllersystemapp.R
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
             UtilKotlin.changeFragment(LoginFragment(),supportFragmentManager,R.id.container)
      //  UtilKotlin.changeFragment(RequestOfferProfileFragment(),supportFragmentManager,R.id.container)
        StatusBarUtil.setTransparent(this)

    }
    companion object {
        val whichFragment = "current_fragment"
    }
}
