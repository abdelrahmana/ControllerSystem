package com.example.controllersystemapp.admin.storesproducts

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.storesproducts.adapters.ViewPagerStoresProductAdapter
import com.example.util.CommonActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_admin_stores_product.*


class AdminStoresProductActivity : CommonActivity(){

    var titlesTabList = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_stores_product)


        setupTabLayout()
        tapClickChangeTextBold()


    }

    private fun tapClickChangeTextBold() {
        productStoreTabLayout?.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view: View? = tab.customView
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text)
                }
                val textView =
                    tab.customView!!.findViewById<TextView>(android.R.id.text1)
                textView.setTextColor(productStoreTabLayout.tabTextColors)
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
        titlesTabList.clear()
        titlesTabList.add(getString(R.string.products))
        titlesTabList.add(getString(R.string.stores))
        val viewPagerAdaptor =
            ViewPagerStoresProductAdapter(
                supportFragmentManager,
                titlesTabList
            )
        viewPagerStoresProduct.adapter = viewPagerAdaptor
        productStoreTabLayout.setupWithViewPager(viewPagerStoresProduct)
        viewPagerStoresProduct.clipToPadding = false
        for (i in 0 until titlesTabList.size) {
            // these maybe not categorize
            productStoreTabLayout.getTabAt(i)!!.text = titlesTabList[i]
        }


    }


}