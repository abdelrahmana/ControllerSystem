package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerAccountantsAdapter
import com.example.controllersystemapp.admin.storesproducts.adapters.ViewPagerStoresProductAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_admin_accountant.*
import kotlinx.android.synthetic.main.fragment_admin_stores_products.*

class AdminAccountantFragment : Fragment() {

    var titlesTab = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_accountant, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        tapClickChangeTextBold()

        backImgAccountant?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }


        }

        addAccountantBtn?.setOnClickListener {
            Toast.makeText(context , "addAccountant" , Toast.LENGTH_LONG).show()
            //go to addAccountant Screen
        }

        viewPagerDelegatesAccountant.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {

                if (i == 0)
                {
                    addAccountantBtn?.visibility = View.GONE
                }
                else if (i == 1)
                {
                    addAccountantBtn?.visibility = View.VISIBLE
                }

            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    private fun tapClickChangeTextBold() {
        delegatesAccountantTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(delegatesAccountantTab.tabTextColors)
                textView.typeface = Typeface.DEFAULT_BOLD
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.typeface = Typeface.DEFAULT
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun setupTabLayout() {
        titlesTab.clear()
        titlesTab.add(getString(R.string.delegates))
        titlesTab.add(getString(R.string.accounatnt))
        val viewPagerAdaptor =
            activity?.supportFragmentManager?.let {
                ViewPagerAccountantsAdapter(
                    it,
                    titlesTab
                )
            }
        viewPagerDelegatesAccountant.adapter = viewPagerAdaptor
        delegatesAccountantTab.setupWithViewPager(viewPagerDelegatesAccountant)
        viewPagerDelegatesAccountant.clipToPadding = false
        for (i in 0 until titlesTab.size) {
            // these maybe not categorize
            delegatesAccountantTab.getTabAt(i)!!.text = titlesTab[i]
        }


    }
}