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
import com.smartangle.controllersystemapp.delegates.wallet.adapter.CurrentWalletAdapter
import com.smartangle.controllersystemapp.delegates.wallet.models.Data
import com.smartangle.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_current_special_wallet.*
import kotlinx.android.synthetic.main.no_products.view.*


class CurrentSpecialWalletFragment : Fragment() , OnRecyclerItemClickListener {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var currentWaletList = ArrayList<Data>()
    lateinit var currentWalletAdapter: CurrentWalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_special_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        handleNoData()
        observeData()


    }

    private fun handleNoData() {
        noCurrentWalletData?.firstNoDataTxt?.text = getString(R.string.no_wallet)
        noCurrentWalletData?.no_data_image?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_wallet_img))
        noCurrentWalletData?.secondNoDataTxt?.visibility = View.GONE
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

        requestCurrentWallet()

    }

    private fun requestCurrentWallet() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            DelegateOrdersPresenter.getOrdersList(webService!! , 1 , activity!! , model)


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun getData(delegateOrdersListResponse : DelegateOrdersListResponse) {

        if (delegateOrdersListResponse.data?.isNullOrEmpty() == false)
        {
            currentWalletRecycler?.visibility = View.VISIBLE
            noCurrentWalletData?.visibility = View.GONE
            currentWaletList.clear()
            currentWaletList.addAll(delegateOrdersListResponse.data)
            currentWalletAdapter = CurrentWalletAdapter(context!! , currentWaletList , this)
            currentWalletRecycler?.apply {

                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
                adapter = currentWalletAdapter

            }
        }
        else{
            //empty
            currentWalletRecycler?.visibility = View.GONE
            noCurrentWalletData?.visibility = View.VISIBLE
        }


    }

    override fun onItemClick(position: Int) {

        Log.d("click" , "position $position name ${currentWaletList[position]}")
        val bundle = Bundle()
        bundle.putInt(WALLET_ORDER_ID, currentWaletList[position].id?:0)
        UtilKotlin.changeFragmentBack(activity!! ,
            OrdersItemsFragment(), ""  ,
            bundle , R.id.frameLayoutDirdelegate)


    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    companion object{

        val WALLET_ORDER_ID = "walletOrderId"

    }


}