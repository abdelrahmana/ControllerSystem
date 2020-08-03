package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerAccountantsAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerDelegateDetailsAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_admin_accountant.*
import kotlinx.android.synthetic.main.fragment_delegate_details.*

class DelegateDetailsFragment : Fragment() {

    var titlesTab = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delegate_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        tapClickChangeTextBold()


        backDelegate?.setOnClickListener {

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



    }

    private fun setupTabLayout() {
        titlesTab.clear()
        titlesTab.add(getString(R.string.orders))
        titlesTab.add(getString(R.string.portfolio))
        val viewPagerAdaptor =
            activity?.supportFragmentManager?.let {
                ViewPagerDelegateDetailsAdapter(
                    it,
                    titlesTab
                )
            }
        viewPagerDelegatesDetails.adapter = viewPagerAdaptor
        delegatesDetailsTab.setupWithViewPager(viewPagerDelegatesDetails)
        viewPagerDelegatesDetails.clipToPadding = false
        for (i in 0 until titlesTab.size) {
            // these maybe not categorize
            delegatesDetailsTab.getTabAt(i)!!.text = titlesTab[i]
        }


    }

    private fun tapClickChangeTextBold() {
        delegatesDetailsTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(delegatesDetailsTab.tabTextColors)
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

}