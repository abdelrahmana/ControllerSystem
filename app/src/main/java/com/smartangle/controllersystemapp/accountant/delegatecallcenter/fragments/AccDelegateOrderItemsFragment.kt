package com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments

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
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters.AccDelegateOrderItemsAdapter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItems
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItemsData
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_acc_delegate_order_items.*
import kotlinx.android.synthetic.main.fragment_acc_delegate_order_items.backImg
import kotlinx.android.synthetic.main.no_products.view.*

class AccDelegateOrderItemsFragment : Fragment() , OnRecyclerItemClickListener {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var orderItemsList = ArrayList<AccDelegateOrderItemsData>()

    lateinit var accDelegateOrderItemsAdapter: AccDelegateOrderItemsAdapter

    var orderId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        orderId = arguments?.getInt(AccDelegateOrdersFragment.ACC_DELEGATE_ORDER_ID, 0)?:0


        return inflater.inflate(R.layout.fragment_acc_delegate_order_items, container, false)
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

    private fun handleNoData() {

        noData?.firstNoDataTxt?.text = getString(R.string.no_orders)
        noData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_order_icon))
        noData?.secondNoDataTxt?.visibility = View.GONE
    }

    private fun requestData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AccountantDelegateOrderPresenter.accountantDelegateOrderItems(webService!! , orderId , activity!! , model)


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

                if (datamodel is AccDelegateOrderItems) {
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

    private fun getData(accDelegateOrderItems: AccDelegateOrderItems) {

        if (accDelegateOrderItems?.data?.isNullOrEmpty() == false)
        {
            accDelegateOrderItemsRecycler?.visibility = View.VISIBLE
            noData?.visibility = View.GONE
            orderItemsList.clear()
            orderItemsList.addAll(accDelegateOrderItems?.data)
            accDelegateOrderItemsAdapter = AccDelegateOrderItemsAdapter(context!! , orderItemsList , this)
            accDelegateOrderItemsRecycler?.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = accDelegateOrderItemsAdapter

            }


        }
        else{
            //empty
            accDelegateOrderItemsRecycler?.visibility = View.GONE
            noData?.visibility = View.VISIBLE

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

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val ACC_DELEGATE_ITEM_ID = "accDelegateItemId"

    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(ACC_DELEGATE_ITEM_ID, orderItemsList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            AccDelagteOrderItemDetailsFragment(), ""  ,
            bundle , R.id.redirect_acc_fragments)

    }


}