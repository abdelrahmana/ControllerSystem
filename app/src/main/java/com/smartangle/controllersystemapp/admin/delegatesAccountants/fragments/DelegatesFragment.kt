package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments

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
import com.google.gson.Gson
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet.Companion.ACCOUNTANT_BLOCK_Delegate
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet.Companion.ACCOUNTANT_REMOVE_DELEGATE
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccountantDelegateOrderPresenter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments.AccDelegateDetailsFragment
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.common.chat.ChatFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_acc_delegates.*
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
                if (datamodel == ACCOUNTANT_BLOCK_Delegate) {

                    blockDelegate()

                }
                else if(datamodel == AccDelegateDetailsBottomSheet.ACCOUNTANT_MessageDelegate){ // send information to chat fragment
                    val bundle = Bundle()
                    bundle.putString(SELECTEDDELEGATE, Gson().toJson(delegatesList?.get(selectedItemPosition)))

                    UtilKotlin.changeFragmentBack(activity!!,
                        ChatFragment(),"callCenterList",bundle,R.id.redirect_acc_fragments) // inside accountant
                }

                modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
            }

        })

    }
    var addDelegateCallCenterFragment = AddDelegateCallCenterRequest()

    private fun blockDelegate() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            addDelegateCallCenterFragment = AddDelegateCallCenterRequest(
                delegatesList[selectedItemPosition].name?:"",
                delegatesList[selectedItemPosition].city_id?.toInt()?:0,
                null, null ,
                delegatesList[selectedItemPosition].phone?:"",
                null , delegatesList[selectedItemPosition].email?:"", 0 ,
                delegatesList[selectedItemPosition].id?:0

            )
            CallCenterPresnter.editDelegate(
                webService!!,
                callCenterResponse(),
                addDelegateCallCenterFragment
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


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


    var disposableBlockObserver : DisposableObserver<Response<SuccessModel>>?=null
    private fun callCenterResponse(): DisposableObserver<Response<SuccessModel>> {

        disposableBlockObserver = object : DisposableObserver<Response<SuccessModel>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<SuccessModel>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    UtilKotlin.showSnackMessage(activity,response.body()?.msg?.get(0)?:getString(R.string.added_successfully))
                    //activity?.supportFragmentManager?.popBackStack()
                    getDelegatesList()

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
        return disposableBlockObserver!!
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
        val SELECTEDDELEGATE= "selected_in_list"
    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        disposableBlockObserver?.dispose()
        modelHandleChangeFragmentclass.let {
            it?.notifyItemSelected.removeObservers(activity!!)
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }

}