package com.smartangle.controllersystemapp.accountant.sales.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.sales.ItemAcceptClickListener
import com.smartangle.controllersystemapp.accountant.sales.adapters.NotReceivedSalesAdapter
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_not_received_sales.*
import kotlinx.android.synthetic.main.no_products.*


class NotReceivedSalesFragment : Fragment(),
    ItemAcceptClickListener {


    lateinit var notReceivedSalesAdapter: NotReceivedSalesAdapter
    var notReceivedSalesList = ArrayList<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_not_received_sales, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleNoDataViews()

    }

    private fun handleNoDataViews() {

        no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.sales_reports_icon))
        firstNoDataTxt?.text = getString(R.string.no_sales)
        secondNoDataTxt?.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()


        setSalesData()

    }

    private fun setSalesData() {


        notReceivedSalesRecycler?.visibility = View.VISIBLE
        noDataLayout?.visibility = View.GONE

        notReceivedSalesList.clear()
        for (i in 0..5)
        {
            notReceivedSalesList.add("")
        }

        notReceivedSalesAdapter =
            NotReceivedSalesAdapter(
                context!!,
                notReceivedSalesList,
                this
            )
        notReceivedSalesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = notReceivedSalesAdapter
        }




    }

    override fun onItemListClick(position: Int) {
        Log.d("click" , "Item")

        UtilKotlin.changeFragmentBack(activity!! ,
            NotReceivedSalesDetailsFragment(), ""  ,
            null , R.id.redirect_acc_fragments)

    }

    override fun onAcceptClick(position: Int) {
        Log.d("click" , "confirm")
    }
}