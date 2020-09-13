package com.example.controllersystemapp.admin.specialcustomers

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.Validation
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_customer.*

class AddCustomerFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_add_customer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImage?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        confirmAddCustomerBtn?.setOnClickListener {

            checkvalidation()

        }

        observeData()
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successAdd(datamodel)
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

    private fun successAdd(successModel: SuccessModel) {


        if (successModel?.msg?.isNullOrEmpty() == false) {
            var msgtext = ""
            for (i in successModel?.msg?.indices) {
                msgtext = msgtext + successModel?.msg?.get(i) + "\n"

            }

//            UtilKotlin.showSnackMessage(activity , msgtext)

            resetAllViews()

        }



    }

    private fun resetAllViews() {


        clientNameEdt?.setText("")
        clientPhoneEdt?.setText("")
        clientAddressEdt?.setText("")
        clientEmailEdt?.setText("")


    }

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
            val addClientRequest = AddClientRequest(
                clientNameEdt?.text?.toString(),
                clientPhoneEdt?.text?.toString(),
                clientEmailEdt?.text?.toString(),
                clientAddressEdt?.text?.toString()
                )

            ClientsPresenter.getAddClient(webService!!, addClientRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }
}