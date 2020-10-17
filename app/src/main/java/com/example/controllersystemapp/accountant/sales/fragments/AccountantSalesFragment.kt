package com.example.controllersystemapp.accountant.sales.fragments

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.sales.ViewPagerAccountantSalesAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_accountant_sales.*


class AccountantSalesFragment : Fragment() {


    lateinit var viewPagerAccountantSalesAdapter: ViewPagerAccountantSalesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accountant_sales, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewPagerAccountantSalesAdapter =
            ViewPagerAccountantSalesAdapter(
                this@AccountantSalesFragment
            )
        salesViewPager?.adapter = viewPagerAccountantSalesAdapter

        TabLayoutMediator(salesTabLayout, salesViewPager) { tab, position ->
            when(position)
            {
                0 -> tab.text = getString(R.string.not_received)

                1 -> tab.text = getString(R.string.received)
            }

        }.attach()


        tapClickChangeTextBold()

        back_image?.setOnClickListener {

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
        salesTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(salesTabLayout?.tabTextColors)
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