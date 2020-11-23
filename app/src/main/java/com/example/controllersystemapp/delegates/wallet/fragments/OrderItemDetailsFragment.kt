package com.example.controllersystemapp.delegates.wallet.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.products.AccountantProductPresenter
import com.example.controllersystemapp.accountant.products.fragments.AccountantProductsFragment
import com.example.controllersystemapp.accountant.products.models.AccountantProdDetailsResponse
import com.example.controllersystemapp.delegates.wallet.DelegateOrdersPresenter
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemDetailsResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_order_item_details.*
import kotlinx.android.synthetic.main.products_item.view.*


class OrderItemDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeIcon?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        confirmDeliveryBtn?.setOnClickListener {

            Log.d("btnClick", "yess")


        }

      //  observeData()



    }

    override fun onResume() {
        super.onResume()

       // getData()

    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            DelegateOrdersPresenter.getOrderItemDetails(
                webService!!,
                arguments?.getInt(OrdersItemsFragment.ITEM_ID, 0) ?: 0
                , activity!!, model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is DelegateOrderItemDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getDetailsData(datamodel)
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

    private fun getDetailsData(itemDetails: DelegateOrderItemDetailsResponse) {

        itemDetails?.data?.let {itemDetailsData ->

            itemName?.text = itemDetailsData?.product?.name?:""
            itemQuantity?.text = (itemDetailsData?.quantity?:0).toString()
            itemCategoryName?.text = itemDetailsData?.product?.category?.name?:""
           // itemStoreName?.text = itemDetailsData?.
            //itemStoreQuantity?.text =
            Glide.with(context!!).load(itemDetailsData?.product?.image?:"")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(itemProdImage)

            itemDescription?.text = itemDetailsData?.product?.description?:""
            itemAddress?.text = itemDetailsData?.order?.address?:""
            itemMapName?.text = itemDetailsData?.order?.name?:""
            itemMapPhone?.text = itemDetailsData?.order?.phone?:""
            itemMapEmail?.text = itemDetailsData?.order?.email?:""
           // feePrice?.text = itemDetailsData?.order?.email?:""
            feeCurrency?.text = itemDetailsData?.order?.currency?:""
            shippingText?.text = itemDetailsData?.order?.shipment_cost?:""
            shippingCurrancy?.text = itemDetailsData?.order?.currency?:""
            totalPriceText?.text = itemDetailsData?.order?.total_price?:""
            totalPriceCurrancy?.text = itemDetailsData?.order?.currency?:""


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