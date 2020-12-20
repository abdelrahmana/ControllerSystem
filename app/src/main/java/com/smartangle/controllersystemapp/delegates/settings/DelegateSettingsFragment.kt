package com.smartangle.controllersystemapp.delegates.settings

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.settings.editpassword.EditPasswordFragment
import com.smartangle.controllersystemapp.admin.settings.editprofile.EditProfileFragment
import com.smartangle.controllersystemapp.common.AuthPresenter
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.PrefsModel
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_delegate_settings.*
import retrofit2.Response


class DelegateSettingsFragment : Fragment() {

    var progressDialog : Dialog?=null
    var authService : WebService?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        authService = ApiManagerDefault(context!!).apiService

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delegate_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backSettings?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        delegateEditProfileCard?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! ,
                EditProfileFragment(), "" , null,R.id.frameLayoutDirdelegate)
        }

        delegateEditPasswordCard?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! ,
                EditPasswordFragment(), "" , null,R.id.frameLayoutDirdelegate)
        }

        delegateLogOutCard?.setOnClickListener {
            Log.d("logout" , "click")
            setLogOut()

        }



    }

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

                        //PrefUtils.setUserToken(context!!, response.body()!!.data.access_token)
                        // PrefUtils.setUserModel(context!!, response.body())
                        PrefsUtil.removeKey(context!! , PrefsModel.TOKEN)
                        PrefsUtil.setLoginState(context!!, false)
                        PrefsUtil.removeKey(context!! , PrefsModel.userModel)

                        //PrefUtils.setUserToken(context!!, response.body()!!.data.access_token)
                        // PrefUtils.setUserModel(context!!, response.body())
                        UtilKotlin.localSignOut(activity)

                    }/* else {
                        UtilKotlin.showSnackErrorInto(activity!! , response.body()?.content?.message?:"")
                         if (response.errorBody() != null) {
                              val error = MyUtils.handleResponseError(response.errorBody(), context!!)
                              // MyUtils.showSnackErrorInto(activity!! , error)
                              MyUtils.showDialog(context, erro  r, R.layout.dialog_error)
                }*/ else {
                        var error = ""
                        error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!! , error)
                        /*  if (response.errorBody() != null) {
                              val error = MyUtils.handleResponseError(response.errorBody(), context!!)
                              // MyUtils.showSnackErrorInto(activity!! , error)
                              MyUtils.showDialog(context, error, R.layout.dialog_error)

                                }*/

                    }
                }
            }


        }


    }

    override fun onDestroyView() {
        signOutObserver()?.dispose()
        super.onDestroyView()
    }


}