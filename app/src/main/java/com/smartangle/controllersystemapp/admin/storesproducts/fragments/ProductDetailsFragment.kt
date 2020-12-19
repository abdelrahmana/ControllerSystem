package com.smartangle.controllersystemapp.admin.storesproducts.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.storesproducts.ProductsPresenter
import com.smartangle.controllersystemapp.admin.storesproducts.adapters.ProductListSliderAdapter
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.ProductsFragment.Companion.PRODUCT_ID
import com.smartangle.controllersystemapp.admin.storesproducts.models.Image
import com.smartangle.controllersystemapp.admin.storesproducts.models.ProductsDetailsResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product_details.*


class ProductDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var productDetailsImages : ArrayList<Image> = ArrayList()
    lateinit var productListSliderAdapter : ProductListSliderAdapter

    var productId = 0

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

        productId = arguments?.getInt(PRODUCT_ID)?:0

        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backDetails?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        deleteProductBtn?.setOnClickListener {

            removeProduct()

        }

        observeData()

        getProductDetailsData()
        editProductButton?.setOnClickListener{
            val bundle = Bundle()
            var productDetails = Gson().toJson(productsDetailsResponse)
            bundle.putString(NameUtils.PRODUCT_DETAILS,productDetails)
            UtilKotlin.changeFragmentBack(activity!! , EditProductFragment() , "editProudcts"  ,
                bundle , R.id.frameLayout_direction)
        }

    }

    private fun removeProduct() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ProductsPresenter.deleteProductPresenter(webService!! ,
                productId , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    var productsDetailsResponse : ProductsDetailsResponse?=null
    private fun observeData() {
        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ProductsDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    setProductsDetailsData(datamodel)
                    productsDetailsResponse= datamodel
                    editProductButton?.isEnabled = true // open it please
                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successRemove(datamodel)
                }
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
                productId ,
                activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }


    private fun successRemove(successModel: SuccessModel) {


        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            //getProductsRequest()
            model.responseCodeDataSetter(null) // start details with this data please
            activity?.supportFragmentManager?.popBackStack()

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