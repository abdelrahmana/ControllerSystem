package com.smartangle.controllersystemapp.common.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.home.AccountantHomeActivity
import com.smartangle.controllersystemapp.admin.AdminHomeActivity
import com.smartangle.controllersystemapp.callcenter.home.CallCenterHome
import com.smartangle.controllersystemapp.common.AuthPresenter
import com.smartangle.controllersystemapp.common.forgetpassword.ForgetPassword
import com.smartangle.controllersystemapp.delegates.activities.DelegatesHomeActivity
import com.smartangle.util.*
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.Validation
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Response


class LoginFragment : Fragment() {


    var phoneValid = false
    var passwordValid = false

    lateinit var rootView : View
    lateinit var progressDialog : Dialog
    var webService: WebService? = null
    var ischeck = false

    lateinit var model: ViewModelHandleChangeFragmentclass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        webService = ApiManagerDefault(activity!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        UtilKotlin.getFirebaseFcmTokenBeforeStart(model)



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton?.setOnClickListener {

            //startActivity(Intent(context , AdminHomeActivity::class.java))

            UtilKotlin.hideKeyboard(rootView)
            checkValidation()
//            startActivity(Intent(context , AdminHomeActivity::class.java))
//            activity!!.finish()

          //  makeLoginLogic()
        }


        forgetPassword?.setOnClickListener {
            UtilKotlin.changeFragmentBack(activity!! , ForgetPassword() , "ForgetPassword"  , null , R.id.container)

            //UtilKotlin.changeFragment(ForgetPassword(), activity?.supportFragmentManager!!, R.id.container)

        }

        show_password?.setOnClickListener {

            Log.d("showPass" , "$ischeck")
            if (ischeck)
            {
                show_password.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_show_pass))
                password_edit_text.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                //password_edit_text.transformationMethod = PasswordTransformationMethod()
               // password_edit_text.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                ischeck = false
                password_edit_text.setSelection(password_edit_text.length())

            }
            else{
                show_password.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_show_pressed))
               // password_edit_text.setTransformationMethod(null);
                password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

                //password_edit_text.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

                ischeck = true
                password_edit_text.setSelection(password_edit_text.length())


            }




        }

        setFcmToken() // listener
    }

    private fun checkValidation() {

        if (phoneNumberEditText?.text.isNullOrBlank()) {
            phoneValid = false
            errorPhone?.visibility = View.VISIBLE
            errorPhone?.text = getString(R.string.phone_required)
        } else if (!Validation.validGlobalPhoneNumber(phoneNumberEditText?.text.toString())) {
            phoneValid = false
            errorPhone?.visibility = View.VISIBLE
            errorPhone?.text = getString(R.string.phone_not_valid)
       } else {
            phoneValid = true
            errorPhone?.visibility = View.GONE

        }


        if (password_edit_text?.text.isNullOrBlank()) {
            passwordValid = false
            errorPassword?.visibility = View.VISIBLE
            errorPassword?.text = getString(R.string.password_required)

        }
//        else if (!Validation.isPasswordValid(passwordText)) {
//            password_text_input.error = getString(R.string.password_not_valid)
//        }
        else {
            passwordValid = true
            errorPassword?.visibility = View.GONE

        }

        if (phoneValid && passwordValid) {
            errorPhone?.visibility = View.GONE
            errorPassword?.visibility = View.GONE

            makeLoginLogic()


        }



    }
    var fcmToken = "" // default empty
    private fun setFcmToken() {
        model?.authListnerLiveData?.observe(activity!!, Observer<Any>{ fcmToken->
            if (fcmToken !=null){
                if (fcmToken is String){
                    this.fcmToken = fcmToken
                }
                // when result is coming
                // here we should set every thing related to this details activity
                //     model?.setObjectData(null)
            }
        })
    }

    private fun makeLoginLogic() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val loginRequest = LoginRequest(phoneNumberEditText?.text?.trim()?.toString() ,
                password_edit_text?.text?.trim()?.toString() , UtilKotlin.getDeviceId(context!!).toString() , fcmToken)
            AuthPresenter.getLoginResponse(webService!! , loginRequest, logInObserver())
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun logInObserver(): DisposableObserver<Response<LoginResponse>> {

        return object : DisposableObserver<Response<LoginResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<LoginResponse>) {
                Log.e("Response", "${response.toString()}")
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()

                    PrefsUtil.setUserToken(context!!, response.body()!!.data?.access_token)
                    PrefsUtil.setLoginState(context!!, true)
                    PrefsUtil.setUserModel(context!!, response.body()?.data?.user)
                   // PrefsUtil.setInt(context!! , NameUtil.Cart_Num_name, response.body()!!.data.user!!.cart_count!!.toInt())
                    PrefsUtil.setString(context!! , NameUtils.NOTIFICATION_STATUS, response.body()!!.data?.user?.enable_notification)

                    whichActivityStart(response?.body()?.data?.user?.role_id?:"1")

                }
                else
                {
                    progressDialog?.dismiss()
                    Log.e("Request", "Request is running")
                    if (response.errorBody() != null) {
                       // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
    }

    private fun whichActivityStart(roleID: String?) {


        if (roleID.equals("1"))
        {
            startActivity(Intent(context , AdminHomeActivity::class.java))
            activity!!.finish()
        }
        else if (roleID.equals("2"))
        {
            startActivity(Intent(context , AccountantHomeActivity::class.java))
            activity!!.finish()
        }
        else if (roleID.equals("3"))
        {
            startActivity(Intent(context , CallCenterHome::class.java))
            activity!!.finish()
        }
        else if (roleID.equals("4"))
        {
            startActivity(Intent(context , DelegatesHomeActivity::class.java))
            activity!!.finish()
        }

    }


    companion object {
    }
}