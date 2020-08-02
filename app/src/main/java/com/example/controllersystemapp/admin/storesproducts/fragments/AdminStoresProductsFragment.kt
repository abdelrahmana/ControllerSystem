package com.example.controllersystemapp.admin.storesproducts.fragments

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
import com.example.controllersystemapp.admin.storesproducts.adapters.ViewPagerStoresProductAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_admin_stores_products.*


class AdminStoresProductsFragment : Fragment() {

    var titlesTabList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_stores_products, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        tapClickChangeTextBold()

        addProductBtn?.setOnClickListener {

            if (viewPagerStoresProduct.currentItem == 0)
            {
                Toast.makeText(context , "addProduct" , Toast.LENGTH_LONG).show()
                //go to adProduct Screen
            }
            else if (viewPagerStoresProduct.currentItem == 1)
            {
                Toast.makeText(context , "addStores" , Toast.LENGTH_LONG).show()
                //go to adStore Screen

            }


        }

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

        viewPagerStoresProduct.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {

                if (i == 0)
                {
                    addProductBtn?.text = getString(R.string.add_product)
                }
                else if (i == 1)
                {
                    addProductBtn?.text = getString(R.string.add_store)
                }

            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

    }


    private fun tapClickChangeTextBold() {
        productStoreTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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
            activity?.supportFragmentManager?.let {
                ViewPagerStoresProductAdapter(
                    it,
                    titlesTabList
                )
            }
        viewPagerStoresProduct.adapter = viewPagerAdaptor
        productStoreTabLayout.setupWithViewPager(viewPagerStoresProduct)
        viewPagerStoresProduct.clipToPadding = false
        for (i in 0 until titlesTabList.size) {
            // these maybe not categorize
            productStoreTabLayout.getTabAt(i)!!.text = titlesTabList[i]
        }


    }

}