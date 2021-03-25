package com.smartangle.controllersystemapp.common.delivery.deliverydetails

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
import com.smartangle.controllersystemapp.common.delivery.AdminDeliveryPresenter
import com.smartangle.controllersystemapp.common.delivery.AdminOrderItemDetailsResponse
import com.smartangle.controllersystemapp.common.delivery.DeliveryAdminOrderItemsFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.admin_order_list.view.*
import kotlinx.android.synthetic.main.delivery_details_fragment.*



class DeliveryDetailsFragment : Fragment() {


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
        orderItemId = arguments?.getInt(DeliveryAdminOrderItemsFragment.ADMIN_ORDER_ITEM_ID, 0)?:0

        return inflater.inflate(R.layout.delivery_details_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        closeImg?.setOnClickListener {

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

            AdminDeliveryPresenter.getAdminOrderItemDetails(
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

                if (datamodel is AdminOrderItemDetailsResponse) {
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

    private fun getDetailsData(detailsData: AdminOrderItemDetailsResponse) {


        detailsData?.data?.let {itemDetailsData ->

            //  orderId = itemDetailsData?.order_id?:0

            deliveryNumb?.text = " أمر تجهيز رقم ${itemDetailsData?.order?.order_number?:""}"
            price?.text = itemDetailsData?.price?:""
            name?.text = itemDetailsData?.product?.name?:""
            deliverCurrancy?.text = itemDetailsData?.order?.currency?:""
            pieceCount?.text = itemDetailsData?.quantity?:""

            itemCategoryName?.text = itemDetailsData?.product?.category?.name?:""
            // itemStoreName?.text = itemDetailsData?.
          //  itemStoreQuantity?.text = itemDetailsData?.product?.total_quantity?:""
            nameCard?.text = itemDetailsData?.product?.name?:""
            priceCard?.text = itemDetailsData?.price?:""
            currancyCard?.text = itemDetailsData?.product?.currency?:""
            Glide.with(context!!).load(itemDetailsData?.product?.image?:"")
//                .placeholder(R.drawable.no_profile)
//                .error(R.drawable.no_profile)
                .into(imageProduct)
            statusTextValues?.text = itemDetailsData?.order?.status_word?:""
            userNameText?.text = itemDetailsData?.order?.delegate?.name?:""
            phoneNumberText?.text = itemDetailsData?.order?.delegate?.phone?:""
            mapName?.text = itemDetailsData?.order?.name?:""
            mapPhone?.text = itemDetailsData?.order?.phone?:""
            mapEmail?.text = itemDetailsData?.order?.email?:""
            mapAddress?.text = itemDetailsData?.order?.address?:""
            feePrice?.text = itemDetailsData?.order?.total_price?:"0.0"
            feeCurrancy?.text = itemDetailsData?.order?.currency?:""
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