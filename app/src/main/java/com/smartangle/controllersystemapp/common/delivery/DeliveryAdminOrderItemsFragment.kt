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
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccountantDelegateOrderPresenter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments.AccDelagteOrderItemDetailsFragment
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments.AccDelegateOrderItemsFragment
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.common.delivery.deliverydetails.DeliveryDetailsFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_delivery_admin_order_items.*
import kotlinx.android.synthetic.main.no_products.view.*


class DeliveryAdminOrderItemsFragment : Fragment()  , OnRecyclerItemClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    lateinit var adminOrderItemsAdapter: AdminOrderItemsAdapter
    var orderItemsList = ArrayList<DataAdminOrderItems>()
    var orderId = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        orderId = arguments?.getInt(DeliveryFragment.DELEVIRY_ADMIN_ORDER_ID, 0)?:0


        return inflater.inflate(R.layout.fragment_delivery_admin_order_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


        handleNoData()
        setViewModelListener()

        requestData()

        //setRecycleViewData()
    }

    private fun requestData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AdminDeliveryPresenter.getAdminOrderItems(webService!! , orderId , activity!! , model)


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }

    private fun setViewModelListener() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AdminOrderItemsResponse) {
                    Log.d("testApi", "isForyou")
                    getData(datamodel)
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

    private fun getData(datamodel: AdminOrderItemsResponse) {

        if (datamodel?.data?.isNullOrEmpty() == false)
        {
            orderItemsAdminRecycleView?.visibility = View.VISIBLE
            noOrderAdminData?.visibility = View.GONE
            orderItemsList.clear()
            orderItemsList.addAll(datamodel?.data)
            adminOrderItemsAdapter = AdminOrderItemsAdapter(orderItemsList , this)
            orderItemsAdminRecycleView?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = adminOrderItemsAdapter
            }
        }
        else{
            //empty
            orderItemsAdminRecycleView?.visibility = View.GONE
            noOrderAdminData?.visibility = View.VISIBLE

//            accDelegateOrderItemsRecycler?.visibility = View.VISIBLE
//            noData?.visibility = View.GONE
//            orderItemsList.clear()
//            orderItemsList.add(AccDelegateOrderItemsData("" , 1 , "1" , "رس" ,
//                null , "1",
//            "" , "" ))
//            accDelegateOrderItemsAdapter = AccDelegateOrderItemsAdapter(context!! , orderItemsList , this)
//            accDelegateOrderItemsRecycler?.apply {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
//                adapter = accDelegateOrderItemsAdapter
//
//            }
        }

    }

    private fun handleNoData() {

        noOrderAdminData?.firstNoDataTxt?.text = getString(R.string.no_orders)
        noOrderAdminData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_wallet_img))
        noOrderAdminData?.secondNoDataTxt?.visibility = View.GONE


    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        bundle.putInt(ADMIN_ORDER_ITEM_ID , orderItemsList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! , DeliveryDetailsFragment() ,
            "DeliveryDetailsFragment" , bundle, R.id.frameLayout_direction)
    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }


    companion object{

        val ADMIN_ORDER_ITEM_ID = "adminOrderItemId"

    }


}