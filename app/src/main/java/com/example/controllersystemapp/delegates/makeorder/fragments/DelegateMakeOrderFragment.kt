package com.example.controllersystemapp.delegates.makeorder.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.delegates.makeorder.DelegateMakeOrderPresenter
import com.example.controllersystemapp.delegates.makeorder.model.Data
import com.example.controllersystemapp.delegates.makeorder.model.DelegateMakeOrderRequest
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.PrefsUtil
import com.example.util.UtilKotlin
import com.example.util.Validation
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_delegate_make_order.*


class DelegateMakeOrderFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var delegateId = 0

    var productsList = ArrayList<Int>()
    var quantityList = ArrayList<Int>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        delegateId = PrefsUtil.getUserModel(context!!)?.id ?: 0

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delegate_make_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backDelegateMakeOrder?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1) {
                activity?.finish()
            } else {
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        productClassifyCard?.setOnClickListener {
            val bundle = Bundle()
            UtilKotlin.changeFragmentBack(
                activity!!, DelegateCategoriesFragment(), "DelegateCategoriesFragment",
                bundle, R.id.frameLayoutDirdelegate
            )
        }


        confirmMakeOrderBtn?.setOnClickListener {

            if (checkValidData()) {

                val delegateMakeOrderRequest = DelegateMakeOrderRequest(
                    nameEdt?.text?.toString(),
                    emailEdt?.text?.toString(), mobileEdt?.text?.toString(),
                    addressEdt?.text?.toString(), shoppingFeeEdt?.text?.toString(),
                    delegateId, productsList, quantityList
                )
                requestMakeOrder(delegateMakeOrderRequest)
                //Toast.makeText(context!! , "true" , Toast.LENGTH_LONG).show()
            }

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
                    getSuccessData(datamodel)
                }
                if (datamodel is Data)
                {
                    getSelectedProducts(datamodel)
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

    private fun getSelectedProducts(datamodel: Data) {

        productsList.clear()
        quantityList.clear()
        productsList?.add(0 , datamodel?.id?:0)
        quantityList.add(0 , datamodel?.selectedQuantity?:1)
        productClassifyText?.text = datamodel?.name?:""
        Log.d("finalSelectedProd" , "id ${datamodel?.id?:0}")
        Log.d("finalSelectedProd" , "quantity ${datamodel?.selectedQuantity?:1}")

    }

    private fun getSuccessData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false) {
            activity?.let {
                UtilKotlin.showSnackMessage(it, successModel?.msg[0])
                model.responseCodeDataSetter(null) // start details with this data please
            }

            Handler().postDelayed(Runnable {
                activity?.let {
                    it.finish()
                }
            }, 1000)

        }

    }

    private fun requestMakeOrder(delegateMakeOrderRequest: DelegateMakeOrderRequest) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            DelegateMakeOrderPresenter.delegateCreateOrder(
                webService!!, delegateMakeOrderRequest
                , activity!!, model
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun checkValidData(): Boolean {

        var errorMessage = ""
        if (nameEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.name_is_required)
            errorMessage += "\n"
        }

        if (mobileEdt?.text.isNullOrBlank()) {
            // phoneValid = false
            errorMessage += getString(R.string.phone_required)
            errorMessage += "\n"
        } else if (!Validation.validGlobalPhoneNumber(mobileEdt?.text.toString())) {
            //phoneValid = false
            errorMessage += getString(R.string.phone_not_valid)
            errorMessage += "\n"
        } else {
            // phoneValid = true
        }


        if (addressEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.address_required)
            errorMessage += "\n"
        }


        if (emailEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.email_name_reuqired)
            errorMessage += "\n"
        } else if (!Validation.validateEmail(emailEdt)) {
            errorMessage += getString(R.string.email_invalid)
            errorMessage += "\n"
        }

        if (shoppingFeeEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.shipping_fee_required)
            errorMessage += "\n"
        }

        if (productsList.isNullOrEmpty())
        {
            errorMessage += getString(R.string.product_classified_required)
            errorMessage += "\n"
        }


        return if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
            false

        } else {
            true
        }


    }
}