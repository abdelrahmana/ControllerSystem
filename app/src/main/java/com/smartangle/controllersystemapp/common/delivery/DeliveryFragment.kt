package com.smartangle.controllersystemapp.common.delivery

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.common.delivery.deliverydetails.DeliveryDetailsFragment
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.DeliveryItemAdapter
import com.smartangle.controllersystemapp.delegates.wallet.fragments.CurrentSpecialWalletFragment
import com.smartangle.controllersystemapp.delegates.wallet.fragments.OrdersItemsFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import kotlinx.android.synthetic.main.fragment_admin_delivery.*
import kotlinx.android.synthetic.main.fragment_admin_delivery.noNewWalletData
import kotlinx.android.synthetic.main.no_products.view.*


class DeliveryFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var deliveryItemAdapter : DeliveryItemAdapter?=null
    val arrayList = ArrayList<Data>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backButton?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }

        }

        handleNoData()
        setViewModelListener()


        //setRecycleViewData()
    }



    private fun handleNoData() {
        noNewWalletData?.firstNoDataTxt?.text = getString(R.string.no_orders)
        noNewWalletData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_wallet_img))
        noNewWalletData?.secondNoDataTxt?.visibility = View.GONE
    }

//    private fun setRecycleViewData() {
//        arrayList.add("")
//        arrayList.add("")
//        arrayList.add("")
//        deliveryItemAdapter = DeliveryItemAdapter(model,arrayList)
//        UtilKotlin.setRecycleView(deliveryRecycleView,deliveryItemAdapter!!,
//            RecyclerView.VERTICAL,context!!, null, true)
//    }


    override fun onDestroyView() {
        model.notifyItemSelected.removeObservers(activity!!)
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()

        requestOrdersList()

    }

    private fun requestOrdersList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AdminDeliveryPresenter.getAdminDeliveryOrdersList(webService!! , 0 , activity!! , model)


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    fun setViewModelListener() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AdminOrdersListResponse) {
                    Log.d("testApi", "isForyou")
                    getData(datamodel)
                }
//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    acceptOrderData(datamodel)
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


        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) {
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }

                    val bundle = Bundle()
                    bundle.putInt(DELEVIRY_ADMIN_ORDER_ID, modelSelected.id?:0)
                    //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                    //  UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , ReportsDetailsFragment() , bundle)
                    UtilKotlin.changeFragmentBack(activity!! , DeliveryAdminOrderItemsFragment() ,
                        "DeliveryAdminOrderItemsFragment"  ,
                        bundle,
                        arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)


                }

                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }
        })
    }

    private fun getData(adminOrdersListResponse: AdminOrdersListResponse) {


        if (adminOrdersListResponse.data?.isNullOrEmpty() == false)
        {
            deliveryRecycleView?.visibility = View.VISIBLE
            noNewWalletData?.visibility = View.GONE
            arrayList.clear()
            arrayList.addAll(adminOrdersListResponse.data)
            deliveryItemAdapter = DeliveryItemAdapter(model,arrayList)
            UtilKotlin.setRecycleView(deliveryRecycleView,deliveryItemAdapter!!,
                RecyclerView.VERTICAL,context!!, null, true)
        }
        else{
            //empty

            deliveryRecycleView?.visibility = View.GONE
            noNewWalletData?.visibility = View.VISIBLE

//            newWalletRecycler?.visibility = View.VISIBLE
//            noNewWalletData?.visibility = View.GONE
//            newWalletList.clear()
//            newWalletList.add(Data("" , "" , 0 , "رس" , 0 ,
//                null , "", 1 , "أمر تجهيز رقم 135655" , 0 , "" ,
//                "" , 0 , "تم التوصيل" , "200" , ""))
//            newWalletAdapter = NewWalletAdapter(context!! , newWalletList , this)
//            newWalletRecycler?.apply {
//
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
//                adapter = newWalletAdapter
//
//            }


        }

    }

    companion object{

        val DELEVIRY_ADMIN_ORDER_ID = "deliveryAdminOrderId"

    }
}