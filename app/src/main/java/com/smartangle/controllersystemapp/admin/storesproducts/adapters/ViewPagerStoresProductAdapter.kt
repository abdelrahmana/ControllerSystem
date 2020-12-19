package com.smartangle.controllersystemapp.admin.storesproducts.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.ProductsFragment
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.StoresFragment

class ViewPagerStoresProductAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {


    private var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 ->
                fragment =
                    ProductsFragment()
            1 ->
                fragment =
                    StoresFragment()

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
//                        ProductsFragment()
//                1 ->
//                    fragment =
//                        StoresFragment()
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
        public val fragmentType = "which_fragment_Tab" // get the current item
    }


}