package com.smartangle.controllersystemapp.callcenter.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.callcenter.delegate.DelegatesFragmentCenter
import com.smartangle.controllersystemapp.callcenter.maketalbya.CallCenterTalbya
import com.smartangle.controllersystemapp.callcenter.setting.SettingCallCenter
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils
import com.smartangle.util.NameUtils.redirectFragmet
import com.smartangle.util.NameUtils.settingsFragmet

class RedirectCallCenter : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect_fragments)

        switchFragments()

    }

    private fun switchFragments() {


        val fragment = intent.getStringExtra(redirectFragmet) ?: ""
        val bundle = Bundle()
        var tag = ""

        when (fragment) {
            NameUtils.ACCOUNTANT_DELEAGTES -> {
                changeFragment(DelegatesFragmentCenter(), fragment)
            }

            NameUtils.MAKE_SPECIAL_ORDER -> {
                changeFragment(CallCenterTalbya(), fragment)
            }

            settingsFragmet-> {
                changeFragment(SettingCallCenter(), fragment)
            }

        }


    }


    private fun changeFragment(fragment: Fragment, tag: String) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout_direction, fragment, tag)
        //transaction.addToBackStack(tag)
        transaction.addToBackStack(tag)
        transaction.commit()

    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount == 1)
        {
            finish()
        }
        else{
            supportFragmentManager.popBackStack()
        }

    }

}