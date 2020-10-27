package com.example.controllersystemapp.accountant.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.makeorder.AccountantMakeOrderFragment
import com.example.controllersystemapp.accountant.noticesandreports.NoticesAndReportsFragment
import com.example.controllersystemapp.accountant.products.fragments.AccountantProductsFragment
import com.example.controllersystemapp.accountant.sales.fragments.AccountantSalesFragment
import com.example.controllersystemapp.accountant.settings.AccountantSettingsFragment
import com.example.controllersystemapp.admin.notification.NotificationFragment
import com.example.controllersystemapp.accountant.delegatecallcenter.DelegateCallCenterFragment
import com.example.controllersystemapp.common.delivery.DeliveryFragment
import com.example.util.CommonActivity
import com.example.util.NameUtils
import com.example.util.NameUtils.ACCOUNTANTS_PRODUCTS
import com.example.util.NameUtils.ACCOUNTANT_SETTINGS
import com.example.util.NameUtils.DELIVERYSETTING
import com.example.util.NameUtils.MAKE_SPECIAL_ORDER
import com.example.util.NameUtils.NOTICES_AND_REPORTS
import com.example.util.NameUtils.SEND_SALES
import com.example.util.NameUtils.WHICHID
import com.example.util.NameUtils.notificationsFragmet
import com.example.util.UtilKotlin

class RedirectAccountantsFragmentActivity : CommonActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect_accountants_fragment)


        switchFragments()

    }


    private fun switchFragments() {


        val fragment = intent.getStringExtra(NameUtils.redirectAccFragmet) ?: ""
        val bundle = Bundle()
        var tag = ""

        when (fragment) {
            ACCOUNTANTS_PRODUCTS -> {
                changeFragment(AccountantProductsFragment(), fragment)
            }

            NameUtils.ACCOUNTANT_DELEAGTES -> {
                changeFragment(DelegateCallCenterFragment(), fragment)
            }
            ACCOUNTANT_SETTINGS -> {
                changeFragment(AccountantSettingsFragment(), fragment)
            }

            SEND_SALES  -> {
                changeFragment(AccountantSalesFragment(), fragment)
            }

            notificationsFragmet  -> {
                changeFragment(NotificationFragment(), fragment)
            }

            MAKE_SPECIAL_ORDER -> {
                changeFragment(AccountantMakeOrderFragment(), fragment)
            }

            NOTICES_AND_REPORTS -> {
                changeFragment(NoticesAndReportsFragment(), fragment)
            }

            DELIVERYSETTING-> {
                val bundle = Bundle()
                bundle.putInt(WHICHID,R.id.redirect_acc_fragments)
                UtilKotlin.changeFragmentWithBack(this,R.id.redirect_acc_fragments,DeliveryFragment(),bundle)
               // changeFragment(DeliveryFragment(), fragment)

            }
        }


    }


    private fun changeFragment(fragment: Fragment, tag: String) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.redirect_acc_fragments, fragment, tag)
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