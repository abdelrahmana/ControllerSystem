package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments

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
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.AdminDelegateOrderItemsFragment
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccountantDelegateDetails
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.DelegateOrder
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.DelegateOrdersAdapter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.ViewPagerDelegateDetailsAdapter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.AdminDelegatePresenter
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_delegates_orders.*
import kotlinx.android.synthetic.main.no_products.view.*

class DelegatesOrdersFragment : Fragment(), OnRecyclerItemClickListener {


    lateinit var rootView: View
    var ordersList = ArrayList<DelegateOrder>()
    lateinit var delegateOrdersAdapter: DelegateOrdersAdapter


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var delegateId = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_delegates_orders, container, false)

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        delegateId = arguments?.getInt(ViewPagerDelegateDetailsAdapter.ADMIN_DELEGATE_DETAILS_ID , 0)?:0

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handleNoData()
        observeData()


    }

    private fun handleNoData() {

        noOrdersDataAdmin?.firstNoDataTxt?.text = getString(R.string.no_orders)
        noOrdersDataAdmin?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_order_icon))
        noOrdersDataAdmin?.secondNoDataTxt?.visibility = View.GONE
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantDelegateDetails) {
                    Log.d("testApi", "isForyou")
                    setData(datamodel)
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


    override fun onResume() {
        super.onResume()

        getOrdersData()

    }

    private fun setData(accountantDelegateDetails: AccountantDelegateDetails) {

        if (accountantDelegateDetails.data?.delegate_orders?.isNullOrEmpty() == false)
        {
            delegateOrdersRecycler?.visibility = View.VISIBLE
            noOrdersDataAdmin?.visibility = View.GONE
            ordersList.clear()
            ordersList.addAll(accountantDelegateDetails.data?.delegate_orders)
            delegateOrdersAdapter = DelegateOrdersAdapter(context!! , ordersList , this)
            delegateOrdersRecycler?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = delegateOrdersAdapter

            }

        }
        else{
            //empty
            delegateOrdersRecycler?.visibility = View.GONE
            noOrdersDataAdmin?.visibility = View.VISIBLE
        }



    }


    private fun getOrdersData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AdminDelegatePresenter.adminDelegateDetails(webService!! ,
                delegateId , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    override fun onItemClick(position: Int){

        Log.d("clickOrder" , "${ordersList[position].name}")
//        UtilKotlin.replaceFragmentWithBack(context!!, this, OrdersDeliveryFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

        val bundle = Bundle()
        bundle.putInt(ADMIN_DELEGATE_ORDER_ITEM_ID , ordersList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            AdminDelegateOrderItemsFragment(), ""  ,
            bundle , arguments?.getInt(
                NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)

        // UtilKotlin.changeFragmentBack(activity!! ,OrdersDeliveryFragment() , "" , null,arguments?.getInt(
        //            NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val ADMIN_DELEGATE_ORDER_ITEM_ID = "adminDelegateOrderItemId"

    }
}