package com.example.controllersystemapp.accountant.delegatecallcenter

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.PrefsUtil
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.edit_call_center.*
import kotlinx.android.synthetic.main.tool_title.*
import retrofit2.Response

class EditDelegateFragment : Fragment() {

    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        return inflater.inflate(R.layout.edit_call_center, container, false)
    }


     var callCenterObject :CallCenterDelegateData? =null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  Log.d("back" , "Delegate crested")
        backImgAddStore?.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        headerText?.text = getString(R.string.edit_delegate)
        add?.text = getString(R.string.confirm)
        add?.setOnClickListener{

            if (UtilKotlin.checkOViewsAvaliablity(callCenterEditText,getString(R.string.name_is_required),activity!!,callCenterError)
                && UtilKotlin.checkOViewsAvaliablity(editTextPhone,getString(R.string.phone_is_required),activity!!,errorPhone)
            ) {
                editCallCenter()
            }
             callCenterObject = UtilKotlin.getDelegateCallCenter(arguments?.getString(NameUtils.CURRENT_DELEGATE)?:"")
            callCenterEditText?.setText(callCenterObject?.name?:"")
            editTextPhone.setText(callCenterObject?.phone?:"")
        }

    }

    var editCallCenterDelegate = AddDelegateCallCenterRequest()

    private fun editCallCenter() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            editCallCenterDelegate = AddDelegateCallCenterRequest(name = callCenterEditText.text.toString(),
                city_id = (PrefsUtil.getUserModel(context!!)?.city_id?:"0").toInt(),phone = editTextPhone.text.toString(),id = callCenterObject?.id?:0)
            CallCenterPresnter.editDelegate(webService!! , callCenterResponse(),editCallCenterDelegate)
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