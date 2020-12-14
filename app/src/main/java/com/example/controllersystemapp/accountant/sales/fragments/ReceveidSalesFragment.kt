package com.example.controllersystemapp.accountant.sales.fragments

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
import com.example.controllersystemapp.accountant.sales.SalesPresenter
import com.example.controllersystemapp.accountant.sales.adapters.ReceivedSalesAdapter
import com.example.controllersystemapp.accountant.sales.model.Data
import com.example.controllersystemapp.accountant.sales.model.SalesResponse
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.callcenter.delegate.order.ItemFragments
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_not_received_sales.*
import kotlinx.android.synthetic.main.fragment_receveid_sales.*
import kotlinx.android.synthetic.main.fragment_receveid_sales.noDataLayout
import kotlinx.android.synthetic.main.no_products.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReceveidSalesFragment : Fragment()  , OnRecyclerItemClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog
    lateinit var receivedSalesAdapter: ReceivedSalesAdapter
    var receivedSalesList = ArrayList<Data>()


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
        return inflater.inflate(R.layout.fragment_receveid_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSalesData()
        observeData()
      //  handleNoDataViews()

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
                SalesPresenter.getSalesList(model!!, webService!!,1,activity!!) // now are we going to add model
                // }
            }
        } else {
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))
        }
    }
    private fun handleNoDataViews() {

        no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!! , R.drawable.sales_reports_icon))
        firstNoDataTxt?.text = getString(R.string.no_sales)
        secondNoDataTxt?.visibility = View.GONE

    }


    override fun onResume() {
        super.onResume()



    }

    private fun setSalesData(datamodel: SalesResponse) {
        receivedSalesRecycler?.visibility = View.VISIBLE
        if (datamodel?.data?.isNotEmpty()==true)
        noDataLayout?.visibility = View.GONE
        else {
            noDataLayout?.visibility = View.VISIBLE
            handleNoDataViews()
            return // no need  to do any thing
        }
        receivedSalesList.clear()
        receivedSalesList.addAll(datamodel.data?:ArrayList())
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
        val bundle = Bundle()
        bundle?.putInt(NameUtils.orderId,receivedSalesList.get(position)?.id?:0)
        bundle?.putString(NameUtils.orderName,receivedSalesList.get(position)?.name?:"")

        UtilKotlin.changeFragmentBack(activity!! ,
            ItemFragments(), ""  ,
            null , R.id.redirect_acc_fragments)
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