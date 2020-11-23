package com.example.controllersystemapp.delegates.wallet.fragments

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
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.delegates.wallet.DelegateOrdersPresenter
import com.example.controllersystemapp.delegates.wallet.adapter.CurrentWalletAdapter
import com.example.controllersystemapp.delegates.wallet.adapter.OrderItemsAdapter
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemsListResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.example.controllersystemapp.delegates.wallet.models.ItemsData
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_current_special_wallet.*
import kotlinx.android.synthetic.main.fragment_orders_items.*
import kotlinx.android.synthetic.main.fragment_orders_items.noCurrentWalletData
import kotlinx.android.synthetic.main.no_products.view.*

class OrdersItemsFragment : Fragment() , OnRecyclerItemClickListener {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var orderItemsList = ArrayList<ItemsData>()
    lateinit var orderItemsAdapter: OrderItemsAdapter

    var orderId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        orderId = arguments?.getInt(CurrentSpecialWalletFragment.WALLET_ORDER_ID , 0)?:0
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backImg?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()
        }

        handleNoData()
        observeData()

        requestData()

    }

    private fun requestData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            DelegateOrdersPresenter.getOrderItemsList(webService!! , orderId , activity!! , model)


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

                if (datamodel is DelegateOrderItemsListResponse) {
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

    private fun getData(delegateOrderItemsListResponse: DelegateOrderItemsListResponse) {

        if (delegateOrderItemsListResponse?.data?.isNullOrEmpty() == false)
        {
            orderItemsRecycler?.visibility = View.VISIBLE
            noCurrentWalletData?.visibility = View.GONE
            orderItemsList.clear()
            orderItemsList.addAll(delegateOrderItemsListResponse?.data)
            orderItemsAdapter = OrderItemsAdapter(context!! , orderItemsList , this)
            orderItemsRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = orderItemsAdapter

            }


        }
        else{
            //empty
            orderItemsRecycler?.visibility = View.VISIBLE
            noCurrentWalletData?.visibility = View.GONE
            orderItemsList.clear()
            orderItemsList.add(ItemsData("" , null , 1 , "رس" ,
                "ايفون سفن بلاس اصل" , 1 ,
            "" , "ايفون" , "500" , "20" , null ))
            orderItemsAdapter = OrderItemsAdapter(context!! , orderItemsList , this)
            orderItemsRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = orderItemsAdapter

            }
        }


    }

    private fun handleNoData() {
        noCurrentWalletData?.firstNoDataTxt?.text = getString(R.string.no_wallet)
        noCurrentWalletData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_wallet_img))
        noCurrentWalletData?.secondNoDataTxt?.visibility = View.GONE
    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val ITEM_ID = "itemId"

    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(ITEM_ID, orderItemsList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            OrderItemDetailsFragment(), ""  ,
            bundle , R.id.frameLayoutDirdelegate)

    }


}