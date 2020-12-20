package com.smartangle.controllersystemapp.delegates.wallet.fragments

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
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.delegates.wallet.DelegateOrdersPresenter
import com.smartangle.controllersystemapp.delegates.wallet.adapter.NewWalletAdapter
import com.smartangle.controllersystemapp.delegates.wallet.models.Data
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_new_special_wallet.*
import kotlinx.android.synthetic.main.no_products.view.*


class NewSpecialWalletFragment : Fragment() , OnRecyclerItemClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var newWalletList = ArrayList<Data>()
    lateinit var newWalletAdapter: NewWalletAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_special_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handleNoData()
        observeData()


    }

    private fun handleNoData() {
        noNewWalletData?.firstNoDataTxt?.text = getString(R.string.no_wallet)
        noNewWalletData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_wallet_img))
        noNewWalletData?.secondNoDataTxt?.visibility = View.GONE
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is DelegateOrdersListResponse) {
                    Log.d("testApi", "isForyou")
                    getData(datamodel)
                }
                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    acceptOrderData(datamodel)
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

    private fun acceptOrderData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            model.responseCodeDataSetter(null) // start details with this data please

            UtilKotlin.showSnackMessage(activity, successModel?.msg[0])
            requestNewWallet()


//            Handler().postDelayed(Runnable {
//                activity?.let {
//                    it.supportFragmentManager.popBackStack()
//                }
//            }, 1000)
        }


    }

    override fun onResume() {
        super.onResume()

        requestNewWallet()

    }

    private fun requestNewWallet() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            DelegateOrdersPresenter.getOrdersList(webService!! , 0 , activity!! , model)


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }

    private fun getData(delegateOrdersListResponse : DelegateOrdersListResponse) {

        if (delegateOrdersListResponse.data?.isNullOrEmpty() == false)
        {
            newWalletRecycler?.visibility = View.VISIBLE
            noNewWalletData?.visibility = View.GONE
            newWalletList.clear()
            newWalletList.addAll(delegateOrdersListResponse.data)
            newWalletAdapter = NewWalletAdapter(context!! , newWalletList , this)
            newWalletRecycler?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = newWalletAdapter

            }
        }
        else{
            //empty

            newWalletRecycler?.visibility = View.GONE
            noNewWalletData?.visibility = View.VISIBLE

//            newWalletRecycler?.visibility = View.VISIBLE
//            noNewWalletData?.visibility = View.GONE
//            newWalletList.clear()
//            newWalletList.add(Data("" , "" , 0 , "رس" , 0 ,
//                null , "", 1 , "أمر تجهيز رقم 135655" , 0 , "" ,
//                "" , 0 , "تم التوصيل" , "200" , ""))
//            newWalletAdapter = NewWalletAdapter(context!! , newWalletList , this)
//            newWalletRecycler?.apply {
//
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
//                adapter = newWalletAdapter
//
//            }


        }


    }

    override fun onItemClick(position: Int) {

        val bundle = Bundle()
        bundle.putInt(CurrentSpecialWalletFragment.WALLET_ORDER_ID, newWalletList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            OrdersItemsFragment(), ""  ,
            bundle , R.id.frameLayoutDirdelegate)

    }

    override fun acceptOrderClickListener(position: Int) {

        acceptOrderRequest(newWalletList[position].id?:0)

    }

    private fun acceptOrderRequest(orderId: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            // status = 3 mean confirm delivery
            // status = 1 mean accept order

            DelegateOrdersPresenter.delegateUpdateOrder(
                webService!!,
                orderId , 1
                , activity!!, model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}