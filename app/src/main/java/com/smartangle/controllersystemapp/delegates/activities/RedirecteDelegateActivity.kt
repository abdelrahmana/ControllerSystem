package com.smartangle.controllersystemapp.delegates.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.notification.NotificationFragment
import com.smartangle.controllersystemapp.delegates.makeorder.fragments.DelegateMakeOrderFragment
import com.smartangle.controllersystemapp.delegates.notificationreports.fragments.DelegateNotifyReportsFragment
import com.smartangle.controllersystemapp.delegates.orders.fragments.DelegateOrdersFragment
import com.smartangle.controllersystemapp.delegates.settings.DelegateSettingsFragment
import com.smartangle.controllersystemapp.delegates.wallet.fragments.SpecialWalletFragment
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils

class RedirecteDelegateActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirecte_delegate)

        switchFragments()

    }

    private fun switchFragments() {


        val fragment = intent.getStringExtra(NameUtils.redirectDelegFragmet) ?: ""
        val bundle = Bundle()
        var tag = ""

        when (fragment) {
            NameUtils.SEPCIAL_WALLET -> {
                changeFragment(SpecialWalletFragment(), fragment)
            }

            NameUtils.DELEGATE_ORDERS -> {
                changeFragment(DelegateOrdersFragment(), fragment)
            }

            NameUtils.DELEGATE_MAKE_ORDER -> {
                changeFragment(DelegateMakeOrderFragment(), fragment)
            }

            NameUtils.DELEGATE_NoTIFY_REPORTS -> {
            changeFragment(DelegateNotifyReportsFragment(), fragment)
        }

            NameUtils.DELEGATE_SETTINGS -> {
                changeFragment(DelegateSettingsFragment(), fragment)
            }

            NameUtils.DELEGATE_NOTIFICATIONS -> {
                changeFragment(NotificationFragment(), fragment)
            }

        }


    }


    private fun changeFragment(fragment: Fragment, tag: String) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutDirdelegate, fragment, tag)
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