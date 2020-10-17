package com.example.controllersystemapp.accountant.delegatecallcenter

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter.getCallCenter
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.add_call_center.*
import kotlinx.android.synthetic.main.tool_title.*
import retrofit2.Response

class AddDelegateFragment : Fragment() {

    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        return inflater.inflate(R.layout.add_call_center, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  Log.d("back" , "Delegate crested")
        backImgAddStore?.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        headerText?.text = getString(R.string.add_delegate)
        add?.text = getString(R.string.add_delegate)
    }


    private fun addDelegate() {

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

}