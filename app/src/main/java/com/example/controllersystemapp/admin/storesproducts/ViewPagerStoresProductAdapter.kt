package com.example.controllersystemapp.admin.storesproducts
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
class ViewPagerStoresProductAdapter(fm:  FragmentManager,var arrayListFragmentName: ArrayList<String>)
    : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {



    private var fragment: Fragment? = null
    // adaptor his views is a fragment
    override fun getItem(position: Int): Fragment { // this is used to return each item as fragment inside the this adaptor
        //var i = position // position 1
//        while (i <= arrayListFragmentName.size-1) {
//            // if (i == position) {
//
//            if (position == 0)
//                fragment =
//                    ProductsFragment()
//
//            if (position == 1)
//                fragment = StoresFragment()
//
////            val args = Bundle()
////            args.putString(fragmentType, arrayListFragmentName.get(i))
////            //args.putParcelable(CATEGORIES, category)
////            fragment!!.arguments = args
//            break
//            // }
//            // i--
//        }
        /*for (i in arrayList.size-1 until 0) { // this is used to send the data in reverse inside the fragment
             if (i == position) {
                 fragment = CandidateFragmentItemNew()
                 val args = Bundle()
                 args.putInt(candidateNumberKey, position)
                 //args.putParcelable(CATEGORIES, category)
                 fragment!!.arguments = args
                 break
             }
         }*/

        for (i in 0 until arrayListFragmentName.size) {
            when (position) {
                0 ->
                    fragment = ProductsFragment()
                1 ->
                    fragment = StoresFragment()

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