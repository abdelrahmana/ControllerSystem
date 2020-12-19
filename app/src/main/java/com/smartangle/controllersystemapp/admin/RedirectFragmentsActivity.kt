package com.smartangle.controllersystemapp.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.categories.fragments.CategoriesFragment
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.AdminAccountantFragment
import com.smartangle.controllersystemapp.common.delivery.DeliveryFragment
import com.smartangle.controllersystemapp.admin.makeorders.AdminMakeOrderFragment
import com.smartangle.controllersystemapp.admin.notification.NotificationFragment
import com.smartangle.controllersystemapp.admin.reports.reportcontainerfragment.ReportContainerFragment
import com.smartangle.controllersystemapp.admin.settings.SettingsFragment
import com.smartangle.controllersystemapp.admin.specialcustomers.AdminSpecicalCustomersragment
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.AdminStoresProductsFragment
import com.smartangle.util.CommonActivity
import com.smartangle.util.NameUtils.categoriesFragmet
import com.smartangle.util.NameUtils.delegatesAccountantsFragmet
import com.smartangle.util.NameUtils.deliveryFragmet
import com.smartangle.util.NameUtils.makeOrderFragmet
import com.smartangle.util.NameUtils.notificationsFragmet
import com.smartangle.util.NameUtils.redirectFragmet
import com.smartangle.util.NameUtils.reporstFragmet
import com.smartangle.util.NameUtils.settingsFragmet
import com.smartangle.util.NameUtils.specialCustomersFragmet
import com.smartangle.util.NameUtils.storeProductsFragmet

class RedirectFragmentsActivity : CommonActivity() {
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
            storeProductsFragmet -> {
                changeFragment(AdminStoresProductsFragment(), fragment)
            }

            delegatesAccountantsFragmet -> {
                changeFragment(AdminAccountantFragment(), fragment)
            }

            deliveryFragmet -> {
                changeFragment(DeliveryFragment(), fragment)
            }

            makeOrderFragmet -> {
                changeFragment(AdminMakeOrderFragment(), fragment)
            }
            reporstFragmet -> {
                changeFragment(ReportContainerFragment(), fragment)
            }
            specialCustomersFragmet -> {
                changeFragment(AdminSpecicalCustomersragment(), fragment)
            }

            categoriesFragmet -> {
                changeFragment(CategoriesFragment(), fragment)
            }

            settingsFragmet-> {
                changeFragment(SettingsFragment(), fragment)
            }

            notificationsFragmet -> {
                changeFragment(NotificationFragment(), fragment)
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