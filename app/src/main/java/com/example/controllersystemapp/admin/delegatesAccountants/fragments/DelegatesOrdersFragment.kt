package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegateOrdersAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.storesproducts.models.ProductsModel
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_delegates_orders.*

class DelegatesOrdersFragment : Fragment(), OnRecyclerItemClickListener {


    lateinit var rootView: View
    var ordersList = ArrayList<ProductsModel>()
    lateinit var delegateOrdersAdapter: DelegateOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_delegates_orders, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    override fun onResume() {
        super.onResume()

        getOrdersData()

    }

    private fun getOrdersData() {

        ordersList.clear()
        ordersList.add(ProductsModel("اسم المنتج", "950" , "رس" ,2 , "بلاغ توصيل رقم 135655" , 1))
        ordersList.add(ProductsModel("سماعات" , "30" , "رس" ,2 , "مخزن الكترونيات / مخزن 1" , 0))

        delegateOrdersAdapter = DelegateOrdersAdapter(context!! , ordersList , this)

        delegateOrdersRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegateOrdersAdapter

        }



    }

    override fun onItemClick(position: Int){

        Log.d("clickOrder" , "${ordersList[position].name}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, OrdersDeliveryFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)
        UtilKotlin.changeFragmentBack(activity!! ,OrdersDeliveryFragment() , "" , null,arguments?.getInt(
            NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)

    }
}