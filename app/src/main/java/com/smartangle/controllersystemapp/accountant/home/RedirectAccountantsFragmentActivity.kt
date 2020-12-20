package com.smartangle.controllersystemapp.accountant.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.createdebts.CreateDebtsFragment
import com.smartangle.controllersystemapp.accountant.makeorder.fragments.AccountantMakeOrderFragment
import com.smartangle.controllersystemapp.accountant.noticesandreports.NoticesAndReportsFragment
import com.smartangle.controllersystemapp.accountant.products.fragments.AccountantProductsFragment
import com.smartangle.controllersystemapp.accountant.sales.fragments.AccountantSalesFragment
import com.smartangle.controllersystemapp.accountant.settings.AccountantSettingsFragment
import com.smartangle.controllersystemapp.admin.notification.NotificationFragment
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments.DelegateCallCenterFragment
import com.smartangle.controllersystemapp.common.delivery.DeliveryFragment
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils
import com.smartangle.util.NameUtils.ACCOUNTANTS_PRODUCTS
import com.smartangle.util.NameUtils.ACCOUNTANT_SETTINGS
import com.smartangle.util.NameUtils.CREATE_DEBTS
import com.smartangle.util.NameUtils.DELIVERYSETTING
import com.smartangle.util.NameUtils.MAKE_SPECIAL_ORDER
import com.smartangle.util.NameUtils.NOTICES_AND_REPORTS
import com.smartangle.util.NameUtils.SEND_SALES
import com.smartangle.util.NameUtils.WHICHID
import com.smartangle.util.NameUtils.notificationsFragmet
import com.smartangle.util.UtilKotlin

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

                changeFragment(
                    AccountantMakeOrderFragment()
                        .also { it.arguments = Bundle().also {
                    it.putInt(NameUtils.WHICHID,R.id.redirect_acc_fragments)} }, fragment)
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

            CREATE_DEBTS-> {
//                val bundle = Bundle()
//                bundle.putInt(WHICHID,R.id.redirect_acc_fragments)
                changeFragment(CreateDebtsFragment(), fragment)
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