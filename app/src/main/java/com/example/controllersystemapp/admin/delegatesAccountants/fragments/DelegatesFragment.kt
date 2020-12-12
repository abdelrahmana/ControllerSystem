package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.*
import com.example.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet.Companion.ACCOUNTANT_REMOVE_DELEGATE
import com.example.controllersystemapp.accountant.delegatecallcenter.fragments.AccDelegateDetailsFragment
import com.example.controllersystemapp.accountant.delegatecallcenter.fragments.EditDelegateFragment
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.admin.specialcustomers.ClientDetailsResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_acc_delegates.*
import kotlinx.android.synthetic.main.fragment_clients_details.*
import retrofit2.Response

class DelegatesFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var delegatesAdapter: DelegatesAdapter
    var delegatesList = ArrayList<CallCenterDelegateData>()

    var webService : WebService?=null
    var progressDialog : Dialog?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!

        return inflater.inflate(R.layout.fragment_acc_delegates, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
       // Log.d("back" , "Delegate crested")


        observeData()


    }

    private fun observeData() {


        modelHandleChangeFragmentclass.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

//                if (datamodel is ClientDetailsResponse) {
//                    Log.d("testApi", "isForyou")
//                    getClientsDetsilsData(datamodel)
//                    clientDetails = datamodel
//                    editClientsButton?.isEnabled = true
//
//                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successRemove(datamodel)
                }

                modelHandleChangeFragmentclass.responseCodeDataSetter(null) // start details with this data please
            }

        })


        modelHandleChangeFragmentclass.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                modelHandleChangeFragmentclass.onError(null)
            }

        })


    }

    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    private fun getDelegatesList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            CallCenterPresnter.getDelegates(webService!!, accDelegatesResponse())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

        modelHandleChangeFragmentclass.notifyItemSelected?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {

//                if (datamodel ==1) {
//
//                    val bundle = Bundle()
//                    bundle.putString(NameUtils.CURRENT_DELEGATE, Gson().toJson(delegatesList.get(selectedItemPosition)))
//                    UtilKotlin.changeFragmentBack(activity!! ,
//                        EditDelegateFragment(),"delegate"  , bundle,
//                        arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
//
//
//                }

                if (datamodel == ACCOUNTANT_REMOVE_DELEGATE) {

                   removeDelegate()


                }

                modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
            }

        })

    }

    private fun removeDelegate() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantDelegateOrderPresenter.accDeleteDelegate(webService!! ,
                delegatesList[selectedItemPosition].id?:0 ,  activity!! , modelHandleChangeFragmentclass)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun successRemove(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            getDelegatesList()

//            model.responseCodeDataSetter(null) // start details with this data please
//            activity?.supportFragmentManager?.popBackStack()

        }





    }

    var selectedItemPosition = 0
    override fun onDestroyView() {
        disposableObserver?.dispose()
        modelHandleChangeFragmentclass.let {
            it?.notifyItemSelected.removeObservers(activity!!)
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<CallCenterResponse>>?=null
    private fun accDelegatesResponse(): DisposableObserver<Response<CallCenterResponse>> {

        disposableObserver= object : DisposableObserver<Response<CallCenterResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<CallCenterResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    delegatesList.clear()
                    delegatesList.addAll(response.body()?.data?.list?:ArrayList())
                    getDeleagtesData()

                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }

    override fun onResume() {
        super.onResume()
        Log.d("back" , "Delegate Resume")
        getDelegatesList()
    }

    private fun getDeleagtesData() {

    //    delegatesList.clear()
    /*    for (i in 0..4)
        {
            delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
        }*/
        centerCount?.text = delegatesList.size.toString()

        delegatesAdapter = DelegatesAdapter(context!! , delegatesList , this)
        accDelegateRecycler?.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegatesAdapter
        }


    }

    override fun onItemClick(position: Int) {

      //  Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

         val bundle = Bundle()
        bundle.putInt(ACCOUNTANT_DELEGATE_ID , delegatesList[position].id?:0)
//        bundle.putInt(NameUtils.WHICHID,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
        UtilKotlin.changeFragmentBack(activity!! , AccDelegateDetailsFragment() , ""  ,
            bundle ,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
    }

    override fun delegateClickListener(position: Int) {
        selectedItemPosition = position
//        val bottomSheetActions = BottomSheetActions()
//        bottomSheetActions.show(activity?.supportFragmentManager!!, "bottomSheetActions")
        val accDelegateDetailsBottomSheet = AccDelegateDetailsBottomSheet()
        accDelegateDetailsBottomSheet.show(activity?.supportFragmentManager!!, "bottomSheetActions")

    }

    companion object{

        val ACCOUNTANT_DELEGATE_ID = "accountantDelegateId"
    }

}