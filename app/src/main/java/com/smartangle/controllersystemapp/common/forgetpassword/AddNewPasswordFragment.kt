package com.smartangle.controllersystemapp.common.forgetpassword

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.common.AuthPresenter
import com.smartangle.controllersystemapp.common.forgetpassword.model.RequestModelNewPass
import com.smartangle.controllersystemapp.common.verficationfragment.VerficationFragment
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_new_pass.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class AddNewPasswordFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var phoneNumber : String?=""
    var requestModelNewPass  :RequestModelNewPass?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        model = UtilKotlin.declarViewModel(this)
        phoneNumber = PrefsUtil.getSharedPrefs(context!!).getString(VerficationFragment.phoneNumberKey, "")?:""
          requestModelNewPass= RequestModelNewPass(phone= phoneNumber)

        return inflater.inflate(R.layout.fragment_add_new_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmButton?.setOnClickListener{

            if (UtilKotlin.checkOViewsAvaliablity(newPasswordEditText,getString(R.string.new_password_required),activity!!,errorNewPass)
                && UtilKotlin.checkOViewsAvaliablity(ConfirmationPasswordEditText,getString(R.string.new_password_required),activity!!,errorConfirmation)
            ) {
                if (newPasswordEditText.text.toString() == ConfirmationPasswordEditText.text.toString()) {
                    requestModelNewPass?.password = newPasswordEditText.text.toString()
                    requestModelNewPass?.password_confirmation = ConfirmationPasswordEditText.text.toString()
                    postNewPass()
                }
                    else {
                    errorConfirmation.text = getString(R.string.confirm_pass_invalid)
                }
            }

        }
        backButton?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }
        createObserverViewModel()
    }
    var callCortinues :Job?=null
    var progressDialog : Dialog?=null
    var model : ViewModelHandleChangeFragmentclass?=null
    var service : WebService?=null
    private fun postNewPass() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
              progressDialog?.show()
            // show loader dialog
            callCortinues = GlobalScope.launch(Dispatchers.Main) {
                // now run this on ui thread
                // should change to user id
                // val result = withContext(Dispatchers.Default) {
                AuthPresenter.setNewPassWord(model!!, service!!,requestModelNewPass!!,activity!!) // now are we going to add model
                // }
            }
        } else {
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))
            progressDialog?.dismiss()
        }
    }

    private fun createObserverViewModel() {
        /*   model.loadPreviousNavBottom.observe(activity!! ,
                      Observer<Int> { position ->
                          if (position !=null) {

                          }

                      })*/
        model?.responseDataCode?.observe(viewLifecycleOwner,Observer<Any>{ datamodel-> // calls
            if (datamodel !=null){
                progressDialog?.dismiss()
                if (datamodel is SuccessModel) // if it is object of this model
                {
                    UtilKotlin.showSnackMessage(activity!!, datamodel.msg?.get(0) ?:"")
                  /*  if (arguments?.getBoolean(NameUtils.FORGETPASSWORD,false) != true)
                        activity!!.supportFragmentManager.popBackStack() // change password
                    else*/
                    activity!!.supportFragmentManager.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    //getData(datamodel) // move data to here please
                }
                // when result is coming
                // here we should set every thing related to this details activity
                model?.responseCodeDataSetter(null)
            }
        })
        model?.errorMessage?.observe(viewLifecycleOwner, Observer<ResponseBody> { error ->
            if (error != null) {
                progressDialog?.dismiss()
                val errorS = UtilKotlin.getErrorBodyResponse(error,context!!)
                UtilKotlin.showSnackErrorInto(activity, errorS)
                model?.onError(null)
            }
        })
        // go to clinic fragment

    }
    fun unSubscribeListners() {
        model.let {
            it?.responseDataCode?.removeObservers(this)
            it?.errorMessage?.removeObservers(this)

        }
        callCortinues.let {
            //   coroutineContext[Job]?.cancel()
            it?.cancel()
        }
    }
    override fun onDestroyView() {
        unSubscribeListners() // remove all listeners
        progressDialog?.dismiss()
        super.onDestroyView()
    }
}