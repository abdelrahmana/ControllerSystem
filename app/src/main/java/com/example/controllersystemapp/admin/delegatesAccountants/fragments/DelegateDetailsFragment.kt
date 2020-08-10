package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.R.attr.fragment
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.RedirectFragmentsActivity
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerDelegateDetailsAdapter
import com.example.util.NameUtils
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_delegate_details.*


class DelegateDetailsFragment : Fragment() {

    var titlesTab = ArrayList<String>()
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView =inflater.inflate(R.layout.fragment_delegate_details, container, false)

        rootView.setFocusableInTouchMode(true)
        rootView.requestFocus()
        rootView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(
                v: View?,
                keyCode: Int,
                event: KeyEvent?
            ): Boolean {
                return if (keyCode == KeyEvent.KEYCODE_BACK) {
                    activity?.finish()
                    val intent = Intent(context , RedirectFragmentsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.putExtra(NameUtils.redirectFragmet, NameUtils.delegatesAccountantsFragmet)
                    startActivity(intent)
                    true
                } else false
            }
        })

        return rootView
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabLayout()
        tapClickChangeTextBold()


        backDelegate?.setOnClickListener {
            activity?.finish()
            val intent = Intent(context , RedirectFragmentsActivity::class.java)
            intent.putExtra(NameUtils.redirectFragmet, NameUtils.delegatesAccountantsFragmet)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)

         //   activity?.supportFragmentManager?.popBackStack()
//            activity?.let {
//
//                it.supportFragmentManager.popBackStack()
////                if (it.supportFragmentManager.backStackEntryCount == 1)
////                {
////                    it.finish()
////                }
////                else{
////                    it.supportFragmentManager.popBackStack()
////                }
//            }


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