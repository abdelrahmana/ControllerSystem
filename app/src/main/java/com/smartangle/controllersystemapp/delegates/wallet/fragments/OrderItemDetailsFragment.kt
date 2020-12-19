package com.smartangle.controllersystemapp.delegates.wallet.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.delegates.wallet.DelegateOrdersPresenter
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrderItemDetailsResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_order_item_details.*


class OrderItemDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var orderId = 0

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
            confirmOrder()

        }

        observeData()



    }

    private fun confirmOrder() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            // status = 3 mean confirm delivery
            DelegateOrdersPresenter.delegateUpdateOrder(
                webService!!,
                orderId , 3
                , activity!!, model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    override fun onResume() {
        super.onResume()

        getData()

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

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    confirmDeliveryData(datamodel)
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

    private fun confirmDeliveryData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            model.responseCodeDataSetter(null) // start details with this data please


            UtilKotlin.showSnackMessage(activity, successModel?.msg[0])
            activity?.supportFragmentManager?.popBackStack()
            activity?.supportFragmentManager?.popBackStack()

//            Handler().postDelayed(Runnable {
//                activity?.let {
//                    it.supportFragmentManager.popBackStack()
//                }
//            }, 1000)
        }


    }

    private fun getDetailsData(itemDetails: DelegateOrderItemDetailsResponse) {

        itemDetails?.data?.let {itemDetailsData ->

            orderId = itemDetailsData?.order_id?:0

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