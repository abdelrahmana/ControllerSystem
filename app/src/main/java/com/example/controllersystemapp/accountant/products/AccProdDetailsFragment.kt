package com.example.controllersystemapp.accountant.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.controllersystemapp.R
import kotlinx.android.synthetic.main.fragment_acc_prod_details.*


class AccProdDetailsFragment : Fragment() {

    lateinit var slidingItemImageAdapter: SlidingItemImageAdapter
    var slideImage = ArrayList<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc_prod_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        setSliderData()
    }

    private fun setSliderData() {

        slideImage.clear()
        slideImage.add("")
        slideImage.add("")
        slideImage.add("")
        slideImage.add("")

        slidingItemImageAdapter = SlidingItemImageAdapter(context!! , slideImage)
        sliderImage?.adapter = slidingItemImageAdapter
        indicator.setViewPager(sliderImage)


//        indicator?.setViewPager(sliderImage as ViewPager)
//        val density = resources.displayMetrics.density
//        //Set circle indicator radius
//        indicator.radius = 5 * density



    }
}