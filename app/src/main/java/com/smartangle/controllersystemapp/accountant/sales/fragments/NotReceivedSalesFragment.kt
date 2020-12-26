package com.smartangle.controllersystemapp.accountant.sales.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.sales.ItemAcceptClickListener
import com.smartangle.controllersystemapp.accountant.sales.SalesPresenter
import com.smartangle.controllersystemapp.accountant.sales.adapters.NotReceivedSalesAdapter
import com.smartangle.controllersystemapp.accountant.sales.model.Data
import com.smartangle.controllersystemapp.accountant.sales.model.SalesResponse
import com.smartangle.controllersystemapp.callcenter.delegate.order.ItemFragments
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_not_received_sales.*
import kotlinx.android.synthetic.main.no_products.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class NotReceivedSalesFragment : Fragment(),
    ItemAcceptClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog
    lateinit var notReceivedSalesAdapter: NotReceivedSalesAdapter
    var notReceivedSalesList = ArrayList<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        return inflater.inflate(R.layout.fragment_not_received_sales, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSalesData()
        observeData()
        //   handleNoDataViews()

    }

    private fun handleNoDataViews() {
        no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.sales_reports_icon))
        firstNoDataTxt?.text = getString(R.string.no_sales)
        secondNoDataTxt?.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()


        //  setSalesData()

    }

    private fun setSalesData(datamodel: SalesResponse) {

        notReceivedSalesRecycler?.visibility = View.VISIBLE
        if (datamodel?.data?.isNotEmpty()==true)
            noDataLayout?.visibility = View.GONE
        else {
            noDataLayout?.visibility = View.VISIBLE
            handleNoDataViews()
            return // no need  to do any thing
        }

        notReceivedSalesList.clear()
        notReceivedSalesList.addAll(datamodel.data?:ArrayList())
        /*    for (i in 0..5)
            {
                notReceivedSalesList.add("")
            }*/

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
    var callCortinues : Job?=null
    private fun getSalesData() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            // show loader dialog
            callCortinues = GlobalScope.launch(Dispatchers.Main) {
                // now run this on ui thread
                // should change to user id
                // val result = withContext(Dispatchers.Default) {
                SalesPresenter.getSalesList(model!!, webService!!,0,activity!!) // now are we going to add model
                // }
            }
        } else {
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))
        }
    }
    override fun onItemListClick(position: Int) {
        val bundle = Bundle()
        bundle?.putInt(NameUtils.orderId,notReceivedSalesList.get(position)?.id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            ItemFragments(), ""  ,
            bundle , R.id.redirect_acc_fragments)

    }

    override fun onAcceptClick(position: Int) {
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {
                progressDialog?.dismiss()

                if (datamodel is SalesResponse) {
                    setSalesData(datamodel)
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.dismiss()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        callCortinues?.cancel()
        super.onDestroyView()
    }
}