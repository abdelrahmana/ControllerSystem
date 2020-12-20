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
import com.smartangle.controllersystemapp.accountant.sales.adapters.ReceivedSalesAdapter
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.UtilKotlin
import kotlinx.android.synthetic.main.fragment_receveid_sales.*
import kotlinx.android.synthetic.main.no_products.*

class ReceveidSalesFragment : Fragment()  , OnRecyclerItemClickListener {


    lateinit var receivedSalesAdapter: ReceivedSalesAdapter
    var receivedSalesList = ArrayList<Any>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receveid_sales, container, false)
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


        receivedSalesRecycler?.visibility = View.VISIBLE
        noDataLayout?.visibility = View.GONE

        receivedSalesList.clear()
        for (i in 0..5)
        {
            receivedSalesList.add("")
        }

        receivedSalesAdapter =
            ReceivedSalesAdapter(
                context!!,
                receivedSalesList,
                this
            )
        receivedSalesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = receivedSalesAdapter
        }




    }

    override fun onItemClick(position: Int) {
        Log.d("click" , "Item")
        UtilKotlin.changeFragmentBack(activity!! ,
            NotReceivedSalesDetailsFragment(), ""  ,
            null , R.id.redirect_acc_fragments)
    }


}