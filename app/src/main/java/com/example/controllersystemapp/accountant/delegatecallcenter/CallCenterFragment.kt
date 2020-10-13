package com.example.controllersystemapp.accountant.delegatecallcenter

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter.getCallCenter
import com.example.controllersystemapp.accountant.delegatecallcenter.adapters.CallCenterAdapter
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterData
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_call_center.*
import retrofit2.Response

class CallCenterFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var callCenterAdapter: CallCenterAdapter
    var callCenterArray = ArrayList<CallCenterData>()
    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        return inflater.inflate(R.layout.fragment_call_center, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  Log.d("back" , "Delegate crested")


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
        callCenterAdapter = CallCenterAdapter(context!! , callCenterArray , this)
        centerRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = callCenterAdapter
        }


    }

    private fun getCallCenter() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            getCallCenter(webService!! , callCenterResponse())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    override fun onDestroyView() {
      disposableObserver?.dispose()
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<CallCenterResponse>>?=null
    private fun callCenterResponse(): DisposableObserver<Response<CallCenterResponse>> {

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
                    callCenterArray.clear()
                    callCenterArray.addAll(response.body()?.data?.list?:ArrayList())
                    getCallCenterData()

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

    override fun onItemClick(position: Int) {

    //    Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

   //     UtilKotlin.changeFragmentBack(activity!! ,DelegateDetailsFragment() , ""  , null,R.id.frameLayout_direction)
    }

}