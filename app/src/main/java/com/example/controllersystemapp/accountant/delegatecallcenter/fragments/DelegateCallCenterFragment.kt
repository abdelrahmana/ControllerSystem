package com.example.controllersystemapp.accountant.delegatecallcenter.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.adapters.ViewPagerDelegateCallAdapter
import com.example.util.UtilKotlin
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_admin_accountant.*

// contains viewpager for delegate and call center
class DelegateCallCenterFragment : Fragment() {

   // var titlesTab = ArrayList<String>()
    lateinit var roorView: View
    lateinit var viewPagerAdaptor : ViewPagerDelegateCallAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        roorView = inflater.inflate(R.layout.fragment_admin_accountant, container, false)

      //  Log.d("back" , "onCreateView")

        return roorView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layout_header?.text = getString(R.string.delegate_call_center)
        addAccountantBtn?.visibility = View.VISIBLE
        addAccountantBtn?.text = getString(R.string.add_delegate)
        viewPagerAdaptor =
            ViewPagerDelegateCallAdapter(
                this@DelegateCallCenterFragment
            )
        viewPagerDelegatesAccountant?.adapter = viewPagerAdaptor

        TabLayoutMediator(delegatesAccountantTab, viewPagerDelegatesAccountant) { tab, position ->
            when(position)
            {
                0 -> {
                    tab.text = getString(R.string.delegates)
                    addAccountantBtn?.text = getString(R.string.add_delegate)
                }
                1 -> {

                    tab.text = getString(R.string.call_center_layout)
                }
            }

        }.attach()

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
            // if 0 delegates , 1 call center
        //   UtilKotlin.changeFragmentBack(activity!! ,AddAccountantFragment() , "AddAccountant"  , null,R.id.frameLayout_direction)

            if (currentSelectedPosition==0)
                UtilKotlin.changeFragmentBack(activity!! ,
                    AddDelegateFragment(), "AddDelegate" , null,R.id.redirect_acc_fragments)
            else
                UtilKotlin.changeFragmentBack(activity!! ,
                    AddCallCenterFragment(), "call_center"  , null,R.id.redirect_acc_fragments)

        }

        viewPagerDelegatesAccountant.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentSelectedPosition = position
                if (position == 0) {
                    addAccountantBtn?.text = getString(R.string.add_delegate)

                  //  addAccountantBtn?.visibility = View.GONE
                }
                else if (position == 1) {
                    //addAccountantBtn?.visibility = View.VISIBLE
                    addAccountantBtn?.text = getString(R.string.add_call_center)

                }

                super.onPageSelected(position)
            }
        })
//        viewPagerDelegatesAccountant.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
//            override fun onPageSelected(i: Int) {
//
//                if (i == 0)
//                {
//                    addAccountantBtn?.visibility = View.GONE
//                }
//                else if (i == 1)
//                {
//                    addAccountantBtn?.visibility = View.VISIBLE
//                }
//
//            }
//
//            override fun onPageScrollStateChanged(i: Int) {}
//        })
    }

    var currentSelectedPosition  = 0

    override fun onResume() {
        super.onResume()
        Log.d("back" , "onResume")
        //setupTabLayout()
        tapClickChangeTextBold()
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

  /*  private fun setupTabLayout() {


//        titlesTab.clear()
//        titlesTab.add(getString(R.string.delegates))
//        titlesTab.add(getString(R.string.accounatnts))

       // viewPagerAdaptor?.notifyDataSetChanged()
//        viewPagerDelegatesAccountant?.adapter = viewPagerAdaptor
//        delegatesAccountantTab?.setupWithViewPager(viewPagerDelegatesAccountant)
//        viewPagerDelegatesAccountant?.clipToPadding = false
//        for (i in 0 until titlesTab.size) {
//            Log.d("back" , "sizr ${titlesTab[i]}")
//
//            // these maybe not categorize
//            delegatesAccountantTab?.getTabAt(i)!!.text = titlesTab[i]
//        }


    }*/
}