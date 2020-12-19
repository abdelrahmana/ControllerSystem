package com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments.CallCenterFragment
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.smartangle.util.NameUtils

class ViewPagerDelegateCallAdapter(fragment: Fragment)
    : FragmentStateAdapter(fragment)  {


    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
                0 -> {
                    val bundle = Bundle()
                    bundle.putInt(NameUtils.WHICHID, R.id.redirect_acc_fragments)
                    fragment =
                        DelegatesFragment().also { it.arguments = bundle }

                }
                1 ->
                    fragment =
                        CallCenterFragment()

            }
        return fragment!!

    }



    // adaptor his views is a fragment
//    override fun getItem(position: Int): Fragment { // this is used to return each item as fragment inside the this adaptor
//        Log.d("back" , "adpter")
//
//        for (i in 0 until arrayListFragmentName.size) {
//            when (position) {
//                0 ->
//                    fragment =
//                        DelegatesFragment()
//                1 ->
//                    fragment =
//                        AccountantsFragment()
//
//            }
//        }
//
//        return fragment!!
//
//    }
//
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








