package com.example.controllersystemapp.admin.settings.editpassword

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.CommonPresenter
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_edit_password.*
import retrofit2.Response

class EditPasswordFragment : Fragment() {
    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPass?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        confirmChangePassBtn?.setOnClickListener{

            if (UtilKotlin.checkOViewsAvaliablity(newPassEdt,getString(R.string.new_password_required),activity!!,errorNewPass)
                && UtilKotlin.checkOViewsAvaliablity(confirmPassEdt,getString(R.string.confirm_pass_required),activity!!,errorConfirmPass)
                && UtilKotlin.checkOViewsAvaliablity(currantPassEdt,getString(R.string.current_password_required),activity!!,errorCurrentPass)
            ) {
                if (newPassEdt.text.toString() == confirmPassEdt.text.toString()) {
                    changePassword()
                }
                else {
                    errorConfirmPass.text = getString(R.string.confirm_pass_invalid)
                }
            }

        }


    }


    var editPassword = EditPasswordRequest()

    private fun changePassword() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            editPassword = EditPasswordRequest(currantPassEdt.text.toString(),
              newPassEdt.text.toString(),confirmPassEdt.text.toString()
                )
            CommonPresenter.editPassword(
                webService!!,
                dispposibleChangePassword(),
                editPassword
            )
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
    private fun dispposibleChangePassword(): DisposableObserver<Response<SuccessModel>> {

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