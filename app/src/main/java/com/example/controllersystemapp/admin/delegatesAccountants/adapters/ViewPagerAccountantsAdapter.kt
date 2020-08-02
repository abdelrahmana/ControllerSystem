package com.example.controllersystemapp.admin.delegatesAccountants.adapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AccountantsFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.example.controllersystemapp.admin.storesproducts.fragments.ProductsFragment
import com.example.controllersystemapp.admin.storesproducts.fragments.StoresFragment

class ViewPagerAccountantsAdapter(fm:  FragmentManager, var arrayListFragmentName: ArrayList<String>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {



    private var fragment: Fragment? = null
    // adaptor his views is a fragment
    override fun getItem(position: Int): Fragment { // this is used to return each item as fragment inside the this adaptor

        for (i in 0 until arrayListFragmentName.size) {
            when (position) {
                0 ->
                    fragment =
                        DelegatesFragment()
                1 ->
                    fragment =
                        AccountantsFragment()

            }
        }

        return fragment!!

    }

    override fun getCount(): Int {
        return arrayListFragmentName.size;
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return arrayListFragmentName[position]
    }
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