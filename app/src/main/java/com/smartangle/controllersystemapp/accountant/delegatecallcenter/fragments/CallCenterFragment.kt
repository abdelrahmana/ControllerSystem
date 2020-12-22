package com.smartangle.controllersystemapp.accountant.delegatecallcenter.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.BottomSheetActions
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter.getCallCenter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.adapters.CallCenterAdapter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.AccDelegateDetailsBottomSheet
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DelegatesFragment
import com.smartangle.controllersystemapp.common.chat.ChatFragment
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_call_center.*
import retrofit2.Response

class CallCenterFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var callCenterAdapter: CallCenterAdapter
    var callCenterArray = ArrayList<CallCenterDelegateData>()
    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    var progressDialog: Dialog? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService // auth
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        return inflater.inflate(R.layout.fragment_call_center, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //  Log.d("back" , "Delegate crested")

        modelHandleChangeFragmentclass.notifyItemSelected?.observe(
            activity!!,
            Observer { datamodel ->

                if (datamodel != null) {

                    if (datamodel == 1) {

                        val bundle = Bundle()
                        bundle.putString(
                            NameUtils.CURRENT_CALL_CENTER,
                            Gson().toJson(callCenterArray.get(selectedItemPosition))
                        )

                        UtilKotlin.changeFragmentBack(
                            activity!!,
                            EditCallCenterFragment(),
                            "call_center",
                            bundle,
                            R.id.redirect_acc_fragments
                        )


                    }else if(datamodel == AccDelegateDetailsBottomSheet.ACCOUNTANT_MessageDelegate){ // send information to chat fragment
                        val bundle = Bundle()
                        bundle.putString(DelegatesFragment.SELECTEDDELEGATE,Gson().toJson(callCenterArray?.get(selectedItemPosition)))

                        UtilKotlin.changeFragmentBack(activity!!,
                            ChatFragment(),"callCenterList",bundle,R.id.redirect_acc_fragments)
                    }

                    modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
                }

            })
    }

    override fun onResume() {
        super.onResume()
        //  Log.d("back" , "Delegate Resume")
        getCallCenter()

    }

    private fun getCallCenterData() {

        /*  delegatesList.clear()
          for (i in 0..4)
          {
              delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
          }*/

        centerCount?.text = callCenterArray.size.toString()
        callCenterAdapter = CallCenterAdapter(context!!, callCenterArray, this)
        centerRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, RecyclerView.VERTICAL, false)
            adapter = callCenterAdapter
        }


    }

    private fun getCallCenter() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            getCallCenter(webService!!, callCenterResponse())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        modelHandleChangeFragmentclass?.notifyItemSelected?.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver: DisposableObserver<Response<CallCenterResponse>>? = null
    private fun callCenterResponse(): DisposableObserver<Response<CallCenterResponse>> {

        disposableObserver = object : DisposableObserver<Response<CallCenterResponse>>() {
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
                    callCenterArray.clear()
                    callCenterArray.addAll(response.body()?.data?.list ?: ArrayList())
                    getCallCenterData()

                } else {
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

    var selectedItemPosition = 0
    override fun onItemClick(position: Int) {
        selectedItemPosition = position
        val bundle = Bundle()
        bundle.putBoolean(callCenter, true)
        val bottomSheetActions =
            BottomSheetActions()
        bottomSheetActions.arguments = bundle
        bottomSheetActions.show(activity?.supportFragmentManager!!, "bottomSheetActions")
        //    Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

        //     UtilKotlin.changeFragmentBack(activity!! ,DelegateDetailsFragment() , ""  , null,R.id.frameLayout_direction)
    }

    companion object {
        val callCenter = "this_is_call_center"
    }

}