package com.example.controllersystemapp.delegates.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.notification.NotificationFragment
import com.example.controllersystemapp.delegates.makeorder.fragments.DelegateMakeOrderFragment
import com.example.controllersystemapp.delegates.notificationreports.fragments.DelegateNotifyReportsFragment
import com.example.controllersystemapp.delegates.orders.fragments.DelegateOrdersFragment
import com.example.controllersystemapp.delegates.settings.DelegateSettingsFragment
import com.example.controllersystemapp.delegates.wallet.fragments.SpecialWalletFragment
import com.example.util.NameUtils

class RedirecteDelegateActivity : AppCompatActivity() {
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