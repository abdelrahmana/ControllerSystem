package com.smartangle.controllersystemapp.accountant.makeorder.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.accountant.makeorder.AccountantMakeOrderPresenter
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.AccountantDelegatesListAdapter
import kotlinx.android.synthetic.main.fragment_acc_select_delegates.*


class AccDelegatesFragment : Fragment() {


    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog
    lateinit var rootView : View

    lateinit var accountantDelegatesListAdapter: AccountantDelegatesListAdapter
    var delegateList = ArrayList<CallCenterDelegateData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_acc_select_delegates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        setViewModelListener()

    }

    override fun onResume() {
        super.onResume()

        getData()


    }

    private fun getData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantMakeOrderPresenter.accountantDelegatesList(webService!!, activity!!, model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    fun setViewModelListener() {
        Log.d("paretnId" , "jj")

        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                Log.d("paretnId" , "observeParent")

                if (modelSelected is CallCenterDelegateData) {

                    setConfirmBtn(modelSelected)

                }
                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null

            }

            Log.d("paretnId" , "j22j")
        })


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is CallCenterResponse) {
                    Log.d("testApi", "isForyou")
                    setRecycleViewData(datamodel)

                }
                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })


    }

    private fun setConfirmBtn(modelSelected: CallCenterDelegateData) {

//        model.setNotifyItemSelected(modelSelected)
//        model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
//        activity?.supportFragmentManager?.popBackStack()
        model?.setNotifyItemSelected(null)
        confirmSelectDelegateBtn?.visibility = View.VISIBLE
        confirmSelectDelegateBtn?.setOnClickListener {
            model?.responseCodeDataSetter(modelSelected)
            activity?.supportFragmentManager?.popBackStack()

        }


    }

    private fun setRecycleViewData(callCenterResponse: CallCenterResponse) {


        if (callCenterResponse.data?.list?.isNullOrEmpty() == false) {

            delegateList.clear()
            delegateList.addAll(callCenterResponse?.data?.list!!)
            accountantDelegatesListAdapter = AccountantDelegatesListAdapter(model, delegateList)
            UtilKotlin.setRecycleView(
                selectDelegateRecycler, accountantDelegatesListAdapter!!,
                RecyclerView.VERTICAL, context!!, null, true
            )
        } else {
            //empty
        }

    }


    override fun onDestroyView() {
        model?.notifyItemSelected?.removeObservers(activity!!)
        model?.responseDataCode?.removeObservers(activity!!)
        model?.errorMessage?.removeObservers(activity!!)

        super.onDestroyView()
    }
}