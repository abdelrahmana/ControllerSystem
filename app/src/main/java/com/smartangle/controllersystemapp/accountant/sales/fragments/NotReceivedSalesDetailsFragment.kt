package com.smartangle.controllersystemapp.accountant.sales.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.sales.SalesPresenter
import com.smartangle.controllersystemapp.callcenter.delegate.model.DataBeans
import com.smartangle.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.*
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.mapAddress
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.mapEmail
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.mapName
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.mapPhone
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.totalPriceCurrancy
import kotlinx.android.synthetic.main.fragment_not_received_sales_details.totalPricePrice
import retrofit2.Response


class NotReceivedSalesDetailsFragment : Fragment() {
    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_received_sales_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_img?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()

        }
        getData() // call get data funciton
    }
    private fun getItemDetails(data: DataBeans) {
        name?.text = data.product?.name?:""
        pieceCount?.text = data.product?.total_quantity?:""
        nameCard?.text = data.product?.name?:""// data.product?.category?.name?:""
        //   prodStoreName?.text = data.ware_houses?.get(0)?.name?:""
        priceCard?.text = data.price.toString()
        deliveryNumb?.text = data.order?.order_number.toString()
        userNameText?.text = data.order?.name?:""
        phoneNumberText?.text = data.order?.phone?:""
        totalPricePrice?.text = data.order?.total_price?:""
        totalPriceCurrancy?.text = data.order?.currency?:""
        currancyCard?.text = data.order?.currency?:""
        mapAddress?.text= data.order?.address?:""
        mapName?.text = data.order?.name?:""
        mapPhone?.text = data.order?.phone?:""
        mapEmail?.text = data.order?.email?:""
        shippingPrice.text = data.order?.shipment_cost?:""
    }
    override fun onDestroyView() {
        disposableObserver?.dispose()
        model.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<ItemDetailsResponse>>?=null
    private fun ItemListDetailsObserver(): DisposableObserver<Response<ItemDetailsResponse>> {

        disposableObserver= object : DisposableObserver<Response<ItemDetailsResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<ItemDetailsResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    getItemDetails(response.body()?.data?: DataBeans())
                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }
    private fun getData() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("item_id",arguments?.getInt(NameUtils.itemId,0)?:0)
            SalesPresenter.getItemDetailsAccountant(webService!!, ItemListDetailsObserver(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }


}