package com.example.controllersystemapp.admin.delegatesAccountants.adapters
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AccountantsFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AdminAccountantFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AdminDelegatesFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment

class ViewPagerAccountantsAdapter(fragment: Fragment)
    : FragmentStateAdapter(fragment)  {


    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
                0 ->
                    fragment = AdminDelegatesFragment()
                        //DelegatesFragment()
                1 ->
                    fragment =
                        AccountantsFragment()

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








