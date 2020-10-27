package com.example.controllersystemapp.admin.delegatesAccountants.adapters
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.*
import com.example.util.NameUtils

class ViewPagerDelegateDetailsAdapter(fragment: Fragment, var id: Int)
    : FragmentStateAdapter(fragment) {



    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt(NameUtils.WHICHID,id)
        when(position)
        {
            0 -> fragment =  DelegatesOrdersFragment().also { it.arguments = bundle }

            1 -> fragment =  DelegateWalletFragment().also { it.arguments = bundle }
        }
        return fragment!!
    }




    // adaptor his views is a fragment
//    override fun getItem(position: Int): Fragment { // this is used to return each item as fragment inside the this adaptor
//
//        for (i in 0 until arrayListFragmentName.size) {
//            when (position) {
//                0 ->
//                    fragment =
//                        DelegatesOrdersFragment()
//                1 ->
//                    fragment =
//                        DelegateWalletFragment()
//
//            }
//        }
//
//        return fragment!!
//
//    }
//
//    override fun getCount(): Int {
//        return arrayListFragmentName.size;
//    }
//    override fun getPageTitle(position: Int): CharSequence? {
//        return arrayListFragmentName[position]
//    }
    /* override fun finishUpdate(container: ViewGroup) {
         try {
             super.finishUpdate(container)
         } catch (nullPointerException: Exception) {
             println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate")
         }
     }*/
    companion object {


        @kotlin.jvm.JvmField
        public val  fragmentType = "which_fragment_Tab" // get the current item
    }


}