package com.example.controllersystemapp.accountant.products.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.products.AccountantProductPresenter
import com.example.controllersystemapp.accountant.products.adapters.SlidingItemImageAdapter
import com.example.controllersystemapp.accountant.products.fragments.AccountantProductsFragment.Companion.ACC_PROD_ID
import com.example.controllersystemapp.accountant.products.models.AccountantProdDetailsResponse
import com.example.controllersystemapp.accountant.products.models.DetailsData
import com.example.controllersystemapp.accountant.products.models.Image
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_acc_prod_details.*


class AccProdDetailsFragment : Fragment() {

    lateinit var slidingItemImageAdapter: SlidingItemImageAdapter
    var slideImage = ArrayList<Image>()

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_acc_prod_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closePageIcon?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        observeData()
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantProdDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getProductDetailsData(datamodel)
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })



    }

    private fun getProductDetailsData(accountantProdDetailsResponse: AccountantProdDetailsResponse) {

        accountantProdDetailsResponse?.data?.let {data ->


            prodName?.text = data.name?:""
            prodQuantity?.text = data.total_quantity?:""
            prodCategoryName?.text = data.category?.name?:""
            prodStoreName?.text = data.ware_houses?.get(0)?.name?:""
            prodStoreQuantity?.text = data.ware_houses?.get(0)?.pivot?.quantity.toString()

            setSliderImages(data)

            prodDetailsDescription?.text = data.description?:""
            totalPricePrice?.text = data.price?:""
            totalPriceCurrancy?.text = data.currency?:""

        }




    }

    private fun setSliderImages(detailsData: DetailsData){

        if (detailsData?.images?.isNullOrEmpty() == false)
        {

            slideImage.clear()
            slideImage.addAll(detailsData?.images)

            slidingItemImageAdapter =
                SlidingItemImageAdapter(
                    context!!,
                    slideImage
                )
            sliderImage?.adapter = slidingItemImageAdapter
            indicator.setViewPager(sliderImage)

        }
        else{
            //empty
            slideImage.clear()
            slideImage.add(
                Image(
                    null,
                    detailsData?.image ?: "",
                    null
                )
            )

            slidingItemImageAdapter =
                SlidingItemImageAdapter(
                    context!!,
                    slideImage
                )
            sliderImage?.adapter = slidingItemImageAdapter
            indicator.setViewPager(sliderImage)
        }


    }

    override fun onResume() {
        super.onResume()

        getData()

    }

    private fun getData() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantProductPresenter.productsListDetails(
                webService!!,
                arguments?.getInt(ACC_PROD_ID, 0) ?: 0
                , activity!!, model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }


}