package com.example.controllersystemapp.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.fragments.CategoriesFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AdminAccountantFragment
import com.example.controllersystemapp.admin.delivery.AdminDeliveryFragment
import com.example.controllersystemapp.admin.makeorders.AdminMakeOrderFragment
import com.example.controllersystemapp.admin.notification.NotificationFragment
import com.example.controllersystemapp.admin.reports.AdminReportsFragment
import com.example.controllersystemapp.admin.reports.reportcontainerfragment.ReportContainerFragment
import com.example.controllersystemapp.admin.settings.SettingsFragment
import com.example.controllersystemapp.admin.specialcustomers.AdminSpecicalCustomersragment
import com.example.controllersystemapp.admin.storesproducts.fragments.AdminStoresProductsFragment
import com.example.util.NameUtils
import com.example.util.NameUtils.categoriesFragmet
import com.example.util.NameUtils.delegatesAccountantsFragmet
import com.example.util.NameUtils.deliveryFragmet
import com.example.util.NameUtils.makeOrderFragmet
import com.example.util.NameUtils.notificationsFragmet
import com.example.util.NameUtils.redirectFragmet
import com.example.util.NameUtils.reporstFragmet
import com.example.util.NameUtils.settingsFragmet
import com.example.util.NameUtils.specialCustomersFragmet
import com.example.util.NameUtils.storeProductsFragmet

class RedirectFragmentsActivity : AppCompatActivity() {
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
                changeFragment(AdminDeliveryFragment(), fragment)
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