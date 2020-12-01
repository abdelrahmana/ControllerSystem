package com.example.controllersystemapp.callcenter.setting

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.settings.expenses.ExpensesFragment
import com.example.controllersystemapp.admin.settings.editpassword.EditPasswordFragment
import com.example.controllersystemapp.admin.settings.editprofile.EditProfileFragment
import com.example.controllersystemapp.common.AuthPresenter
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_accountant_settings.*
import retrofit2.Response


class SettingCallCenter : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var authService : WebService?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        authService = ApiManagerDefault(context!!).apiService
        return inflater.inflate(R.layout.fragment_accountant_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImage?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        expensesCard?.visibility = View.GONE
        notificationCard.visibility = View.GONE
        editProfileCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                EditProfileFragment(), "" , null,R.id.frameLayout_direction)
        }

        editPasswordCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                EditPasswordFragment(), "" , null,R.id.frameLayout_direction)
        }

        logOutCard?.setOnClickListener {
            setLogOut()
        }
    }

    var progressDialog : Dialog?=null
    private fun setLogOut() {
        if(UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AuthPresenter.postLogOut(authService!!,signOutObserver()!!)
        }
        else{
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun signOutObserver(): DisposableObserver<Response<SuccessModel>>? {

        return object : DisposableObserver<Response<SuccessModel>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                progressDialog?.dismiss()
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                dispose()

            }

            override fun onNext(response: Response<SuccessModel>) {
                progressDialog?.dismiss()
                if (response.isSuccessful)
                {
                    if (response.isSuccessful) { // success

                        UtilKotlin.localSignOut(activity)

                    } else {
                        var error = ""
                        error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!! , error)

                    }
                }
            }


        }

    }
}