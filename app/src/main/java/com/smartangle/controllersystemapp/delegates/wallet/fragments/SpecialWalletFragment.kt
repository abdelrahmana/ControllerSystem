package com.smartangle.controllersystemapp.delegates.wallet.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.delegates.wallet.adapter.ViewPagerSpecialWalletAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_speacial_wallet.*
import kotlinx.android.synthetic.main.fragment_speacial_wallet.backImg


class SpecialWalletFragment : Fragment() {


    lateinit var viewPagerSpecialWalletAdapter: ViewPagerSpecialWalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_speacial_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerSpecialWalletAdapter = ViewPagerSpecialWalletAdapter(this@SpecialWalletFragment)
        specialWalletViewPager?.adapter = viewPagerSpecialWalletAdapter

        TabLayoutMediator(specialWalletTab, specialWalletViewPager) { tab, position ->
            when(position)
            {
                0 -> tab.text = getString(R.string.current)

                1 -> tab.text = getString(R.string.addNew)
            }

        }.attach()


        tapClickChangeTextBold()


        backImg?.setOnClickListener {

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

    private fun tapClickChangeTextBold() {
        specialWalletTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(specialWalletTab?.tabTextColors)
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