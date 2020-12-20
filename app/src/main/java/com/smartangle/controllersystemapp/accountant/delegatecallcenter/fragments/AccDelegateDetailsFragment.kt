package com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters.ViewPagerAccDelegateDetailsAdapter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_acc_delegate_details.*
import kotlinx.android.synthetic.main.fragment_acc_delegate_details.backDelegate


class AccDelegateDetailsFragment : Fragment() {

    lateinit var viewPagerAdaptor : ViewPagerAccDelegateDetailsAdapter

    var accDelegateId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        accDelegateId = arguments?.getInt(DelegatesFragment.ACCOUNTANT_DELEGATE_ID , 0)?:0
        return inflater.inflate(R.layout.fragment_acc_delegate_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdaptor = ViewPagerAccDelegateDetailsAdapter(this@AccDelegateDetailsFragment , accDelegateId)

        accDelegateDetailsPager?.adapter = viewPagerAdaptor

        TabLayoutMediator(accDelegateDetailsTab, accDelegateDetailsPager) { tab, position ->
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

    private fun tapClickChangeTextBold() {
        accDelegateDetailsTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(accDelegateDetailsTab.tabTextColors)
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