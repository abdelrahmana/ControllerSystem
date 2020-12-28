package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters.AccDelegateOrderItemsAdapter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItemsData
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesOrdersFragment
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.OrdersDeliveryFragment
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_admin_delegate_order_items.*
import kotlinx.android.synthetic.main.no_products.view.*

class AdminDelegateOrderItemsFragment : Fragment() , OnRecyclerItemClickListener {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var orderId = 0

    lateinit var adminDelegateOrderItemsAdapter: AdminDelegateOrderItemsAdapter
    var orderItemsList = ArrayList<Any>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        orderId = arguments?.getInt(DelegatesOrdersFragment.ADMIN_DELEGATE_ORDER_ITEM_ID, 0)?:0


        return inflater.inflate(R.layout.fragment_admin_delegate_order_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backImg?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()
        }

        handleNoData()

        //observeData()

        requestData()

    }

    private fun requestData() {

        adminDelegateOrderItemsRecycler?.visibility = View.VISIBLE
        noDataAdminDelegateItems?.visibility = View.GONE
        orderItemsList.clear()
        orderItemsList.add("")
        adminDelegateOrderItemsAdapter = AdminDelegateOrderItemsAdapter(context!! , orderItemsList , this)
        adminDelegateOrderItemsRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = adminDelegateOrderItemsAdapter

        }

    }

    private fun handleNoData() {

        noDataAdminDelegateItems?.firstNoDataTxt?.text = getString(R.string.no_orders)
        noDataAdminDelegateItems?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_order_icon))
        noDataAdminDelegateItems?.secondNoDataTxt?.visibility = View.GONE
    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        //bundle.putInt(ADMIN_DELEGATE_ORDER_ITEM_ID_DETAILS, orderItemsList[position].id?:0)
        bundle.putInt(ADMIN_DELEGATE_ORDER_ITEM_ID_DETAILS, 0)
        UtilKotlin.changeFragmentBack(activity!!
             , OrdersDeliveryFragment() ,
             "" , null,R.id.frameLayout_direction)

    }

    companion object{

        val ADMIN_DELEGATE_ORDER_ITEM_ID_DETAILS = "adminDelegateOrderItemIDDetails"

    }

}