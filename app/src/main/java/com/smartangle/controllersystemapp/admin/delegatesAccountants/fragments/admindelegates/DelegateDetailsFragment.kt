package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerDelegateDetailsAdapter
import com.smartangle.util.NameUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.AdminDelegatesFragment.Companion.ADMIN_DELEGATE_ID
import kotlinx.android.synthetic.main.fragment_delegate_details.*


class DelegateDetailsFragment : Fragment() {

    //var titlesTab = ArrayList<String>()
    lateinit var rootView: View
    lateinit var viewPagerAdaptor : ViewPagerDelegateDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView =inflater.inflate(R.layout.fragment_delegate_details, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdaptor = ViewPagerDelegateDetailsAdapter(this@DelegateDetailsFragment,
            arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction ,
        arguments?.getInt(ADMIN_DELEGATE_ID, 0)?:0)
        viewPagerDelegatesDetails?.adapter = viewPagerAdaptor

        TabLayoutMediator(delegatesDetailsTab, viewPagerDelegatesDetails) { tab, position ->
            when(position)
            {
                0 -> tab.text = getString(R.string.orders)

                1 -> tab.text = getString(R.string.portfolio)
            }

        }.attach()

        //setupTabLayout()
        tapClickChangeTextBold()


        backDelegate?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }



    }

//    private fun setupTabLayout() {
//        titlesTab.clear()
//        titlesTab.add(getString(R.string.orders))
//        titlesTab.add(getString(R.string.portfolio))
//        val viewPagerAdaptor = ViewPagerDelegateDetailsAdapter(activity?.supportFragmentManager!!, titlesTab)
//        viewPagerDelegatesDetails.adapter = viewPagerAdaptor
//        delegatesDetailsTab.setupWithViewPager(viewPagerDelegatesDetails)
//        viewPagerDelegatesDetails.clipToPadding = false
//        for (i in 0 until titlesTab.size) {
//            // these maybe not categorize
//            delegatesDetailsTab.getTabAt(i)!!.text = titlesTab[i]
//        }
//
//
//    }

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