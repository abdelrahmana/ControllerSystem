package com.example.controllersystemapp.accountant.delegatecallcenter.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter.addCallCenterApi
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.PrefsUtil
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.add_call_center.*
import kotlinx.android.synthetic.main.tool_title.*
import retrofit2.Response

class AddCallCenterFragment : Fragment() {

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
        headerText?.text = getString(R.string.add_call_center)

        add?.setOnClickListener{

            if (UtilKotlin.checkOViewsAvaliablity(callCenterEditText,getString(R.string.name_is_required),activity!!,callCenterError)
                && UtilKotlin.checkOViewsAvaliablity(editTextPhone,getString(R.string.phone_is_required),activity!!,errorPhone)
                && UtilKotlin.checkOViewsAvaliablity(editTextPassword,getString(R.string.new_password_required),activity!!,errorPassword)
            ) {
                addCallCenter()
            }

        }

    }

    var addDelegateCallCenterFragment = AddDelegateCallCenterRequest()

    private fun addCallCenter() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            addDelegateCallCenterFragment = AddDelegateCallCenterRequest(callCenterEditText.text.toString(),
                (PrefsUtil.getUserModel(context!!)?.city_id?:"0").toInt(),editTextPassword.text.toString()
                ,editTextPassword.text.toString(),editTextPhone.text.toString(),(PrefsUtil.getUserModel(context!!)?.role_id?:"0").toInt())
            addCallCenterApi(webService!! , callCenterResponse(),addDelegateCallCenterFragment)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }


    override fun onDestroyView() {
      disposableObserver?.dispose()
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<SuccessModel>>?=null
    private fun callCenterResponse(): DisposableObserver<Response<SuccessModel>> {

        disposableObserver= object : DisposableObserver<Response<SuccessModel>>() {
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
                    activity?.supportFragmentManager?.popBackStack()
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