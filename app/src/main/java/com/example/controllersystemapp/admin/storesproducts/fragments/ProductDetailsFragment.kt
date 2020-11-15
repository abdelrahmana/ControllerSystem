package com.example.controllersystemapp.admin.storesproducts.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.storesproducts.ProductsPresenter
import com.example.controllersystemapp.admin.storesproducts.adapters.ProductListSliderAdapter
import com.example.controllersystemapp.admin.storesproducts.fragments.ProductsFragment.Companion.PRODUCT_ID
import com.example.controllersystemapp.admin.storesproducts.models.Image
import com.example.controllersystemapp.admin.storesproducts.models.ProductsDetailsResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_product_details.*


class ProductDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var productDetailsImages : ArrayList<Image> = ArrayList()
    lateinit var productListSliderAdapter : ProductListSliderAdapter


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

        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backDetails?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        observeData()

        getProductDetailsData()


    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ProductsDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    setProductsDetailsData(datamodel)
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

    private fun setProductsDetailsData(productsDetailsResponse: ProductsDetailsResponse) {

        setImageSlider(productsDetailsResponse)


        DetailsNameTxt?.text = productsDetailsResponse?.data?.name?:""
        DetailsDescTxt?.text = productsDetailsResponse?.data?.description?:""
        DetailsPriceTxt?.text = productsDetailsResponse?.data?.price?:""
        DetailsPriceCurrencyTxt?.text = productsDetailsResponse?.data?.currency?:""
        DetailsCategoryTxt?.text = productsDetailsResponse?.data?.category?.name?:""
        quantityText?.text = productsDetailsResponse?.data?.total_quantity?:""

        setWarehouseData(productsDetailsResponse)






    }

    private fun setWarehouseData(productsDetailsResponse: ProductsDetailsResponse) {

        if (productsDetailsResponse?.data?.ware_houses?.isNullOrEmpty() == false)
        {
            var categoriesName = ""
            for (i in 0 until productsDetailsResponse?.data?.ware_houses?.size!!) {
                categoriesName += productsDetailsResponse?.data?.ware_houses?.get(i).name?:""
                if (i != productsDetailsResponse?.data?.ware_houses?.size - 1)
                    categoriesName += " - "
            }

            storesNameTxt?.text = categoriesName
        }
        else{
            //empty
        }

    }

    private fun setImageSlider(productsDetailsResponse: ProductsDetailsResponse) {

        if (productsDetailsResponse?.data?.images?.isNullOrEmpty() == false)
        {

            productDetailsImages.clear()
            productDetailsImages.addAll(productsDetailsResponse?.data?.images)
            productListSliderAdapter = ProductListSliderAdapter(context!! , productDetailsImages)
            detailsSlider?.sliderAdapter = productListSliderAdapter
            //productListSliderAdapter.notifyDataSetChanged()

        }
        else{
            //empty list
        }



    }

    private fun getProductDetailsData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            ProductsPresenter.getProductDetails(webService!! ,
                arguments?.getInt(PRODUCT_ID)?:0 ,
                activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }


    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}