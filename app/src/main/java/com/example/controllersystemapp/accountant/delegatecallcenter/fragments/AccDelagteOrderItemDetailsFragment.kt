package com.example.controllersystemapp.accountant.delegatecallcenter.fragments

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
import com.example.controllersystemapp.accountant.delegatecallcenter.AccountantDelegateOrderPresenter
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItemsDetails
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_acc_delagte_order_item_details.*


class AccDelagteOrderItemDetailsFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var orderItemId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        orderItemId = arguments?.getInt(AccDelegateOrderItemsFragment.ACC_DELEGATE_ITEM_ID , 0)?:0
        return inflater.inflate(R.layout.fragment_acc_delagte_order_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeIcon?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }


        observeData()

    }

    override fun onResume() {
        super.onResume()

        getData()

    }


    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantDelegateOrderPresenter.accDelegateOrderItemDetails(
                webService!!, orderItemId, activity!!, model)

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

                if (datamodel is AccDelegateOrderItemsDetails) {
                    Log.d("testApi", "isForyou")
                    getDetailsData(datamodel)
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    confirmDeliveryData(datamodel)
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

    private fun getDetailsData(accDelegateOrderItemsDetails: AccDelegateOrderItemsDetails) {

        accDelegateOrderItemsDetails?.data?.let {itemDetailsData ->

          //  orderId = itemDetailsData?.order_id?:0

            itemName?.text = itemDetailsData?.product?.name?:""
            itemQuantity?.text = itemDetailsData?.quantity?:""
            itemCategoryName?.text = itemDetailsData?.product?.category?.name?:""
            // itemStoreName?.text = itemDetailsData?.
            itemStoreQuantity?.text = itemDetailsData?.product?.total_quantity?:""
            Glide.with(context!!).load(itemDetailsData?.product?.image?:"")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(itemProdImage)

            itemDescription?.text = itemDetailsData?.product?.description?:""
            itemAddress?.text = itemDetailsData?.order?.address?:""
            itemMapName?.text = itemDetailsData?.order?.name?:""
            itemMapPhone?.text = itemDetailsData?.order?.phone?:""
            itemMapEmail?.text = itemDetailsData?.order?.email?:""
            feePrice?.text = itemDetailsData?.order?.total_price?:"0.0"
            feeCurrency?.text = itemDetailsData?.order?.currency?:""
            shippingPrice?.text = itemDetailsData?.order?.shipment_cost?:"0.0"
            shippingCurrancy?.text = itemDetailsData?.order?.currency?:""
            val price = itemDetailsData?.order?.total_price?:"0"
            val shippingPrice = itemDetailsData?.order?.shipment_cost?:"0"
            val totalPrice = price.toDouble() + shippingPrice.toDouble()
            totalPricePrice?.text = (totalPrice).toString()
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