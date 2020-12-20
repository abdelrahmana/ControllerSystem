package com.smartangle.controllersystemapp.admin.specialcustomers

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.Validation
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_edit_client.*

class EditClientFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var clientId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_edit_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImage?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        confirmAddCustomerBtn?.setOnClickListener {

            checkvalidation()

        }

        setClientDetails()

        observeData()
    }

    private fun setClientDetails() {

        val clientDetailsString = arguments?.getString(NameUtils.ADMIN_CLIENTS_DETAILS)?:""
        val clientDetails = UtilKotlin.getClientDetails(clientDetailsString)

        clientId = clientDetails?.data?.id?:0
        clientNameEdt?.setText(clientDetails?.data?.name?:"")
        clientPhoneEdt?.setText(clientDetails?.data?.phone?:"")
        clientAddressEdt?.setText(clientDetails?.data?.address?:"")
        clientEmailEdt?.setText(clientDetails?.data?.email?:"")


    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successEdit(datamodel)
                }

                model.responseCodeDataSetter(null) // start details with this data please
            }

        })

        model.errorMessage.observe(activity!!, Observer { error ->

            if (error != null) {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })



    }

    private fun successEdit(successModel: SuccessModel) {


        if (successModel?.msg?.isNullOrEmpty() == false) {
            var msgtext = ""
            for (i in successModel?.msg?.indices) {
                msgtext = msgtext + successModel?.msg?.get(i) + "\n"

            }

            UtilKotlin.showSnackMessage(activity , msgtext)
            model.responseCodeDataSetter(null) // start details with this data please
            Handler().postDelayed(Runnable {
                activity?.let {
                    it.supportFragmentManager.popBackStack()
                }
            }, 1000)

//            UtilKotlin.showSnackMessage(activity , msgtext)

         //   resetAllViews()

        }



    }

//    private fun resetAllViews() {
//
//
//        clientNameEdt?.setText("")
//        clientPhoneEdt?.setText("")
//        clientAddressEdt?.setText("")
//        clientEmailEdt?.setText("")
//
//
//    }

    private fun checkvalidation() {

        var errorMessage = ""

        if (clientNameEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.client_name_required)
            errorMessage += "\n"
        }

        if (clientPhoneEdt?.text.isNullOrBlank()) {
            // phoneValid = false
            errorMessage += getString(R.string.phone_required)
            errorMessage += "\n"
        } else if (!Validation.validGlobalPhoneNumber(clientPhoneEdt?.text.toString())) {
            //phoneValid = false
            errorMessage += getString(R.string.phone_not_valid)
            errorMessage += "\n"
        } else {
            // phoneValid = true
        }


        if (clientAddressEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.client_address_required)
            errorMessage += "\n"
        }


        if(clientEmailEdt?.text.isNullOrBlank()){
            errorMessage += getString(R.string.email_name_reuqired)
            errorMessage += "\n"
        }
        else if (!Validation.validateEmail(clientEmailEdt))
        {
            errorMessage += getString(R.string.email_invalid)
            errorMessage += "\n"
        }


        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {

            requestAddClient()

        }




    }

    private fun requestAddClient() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            val editClientRequest = EditClientRequest(
                clientId , clientNameEdt?.text?.toString(),
                clientPhoneEdt?.text?.toString(), clientAddressEdt?.text?.toString(),
                clientEmailEdt?.text?.toString()
            )

            ClientsPresenter.getEditClient(webService!!, editClientRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }


}