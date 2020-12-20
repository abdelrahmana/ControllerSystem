package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_accountant_details.*


class AccountantDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var accountantId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        accountantId = arguments?.getInt(NameUtils.ACCOUNTANT_ID)?:0

        return inflater.inflate(R.layout.fragment_accountant_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        confirmAddAccountantBtn?.visibility = View.GONE
//        spinnerImg?.visibility = View.GONE

        backImgAddAccountant?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        deleteAccountantBtn?.setOnClickListener {

            removeAccountant()

        }

        editAccountantButton?.setOnClickListener {

            val bundle = Bundle()
            val accountantDetails = Gson().toJson(accountantResponseDetails)
            bundle.putString(NameUtils.ACCOUNTANT_ADMIN_DETAILS , accountantDetails)
            UtilKotlin.changeFragmentBack(activity!! , EditAccountantFragment() , "EditAccountant"  ,
                bundle , R.id.frameLayout_direction)

        }

        observeData()
    }

    private fun removeAccountant() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantPresenter.deleteAccountantPresenter(webService!! ,
                accountantId , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    var accountantResponseDetails : AccountantDetailsResponse ? = null
    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AccountantDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getAccountantData(datamodel)
                    accountantResponseDetails = datamodel
                    editAccountantButton?.isEnabled = true
                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successRemove(datamodel)
                }

                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


        model.errorMessage.observe(activity!! , Observer { error ->

            if (error != null)
            {
                progressDialog?.hide()
                val errorFinal = UtilKotlin.getErrorBodyResponse(error, context!!)
                UtilKotlin.showSnackErrorInto(activity!!, errorFinal)

                model.onError(null)
            }

        })



    }

    private fun getAccountantData(accountantDetailsResponse: AccountantDetailsResponse) {

        accountantDetailsName?.text = accountantDetailsResponse?.data?.name?:""
        accountantPhoneName?.text = accountantDetailsResponse?.data?.phone?:""
        accountantDetailsLocation?.text = accountantDetailsResponse?.data?.city?.name?:""
        accountantDetailsEmail?.text = accountantDetailsResponse?.data?.email?:""
        Glide.with(context!!).load(accountantDetailsResponse?.data?.image?:"")
            //.placeholder(R.drawable.image_delivery_item)
            .dontAnimate().into(accountantDetailsImage)

        //nameEdt?.isFocusable = false
        //nameEdt?.setText(accountantDetailsResponse?.data?.name?:"")

        //accountantPhoneEdt?.isFocusable = false
        //accountantPhoneEdt?.setText(accountantDetailsResponse?.data?.phone?:"")

//        spinnerImg?.visibility = View.GONE
//        cityEditText?.isFocusable = false
//        cityEditText?.setText(accountantDetailsResponse?.data?.city?.name?:"")

    }

    override fun onResume() {
        super.onResume()

        getDetails()

    }

    private fun getDetails() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AccountantPresenter.getAccountantDetails(webService!! ,
                accountantId , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun successRemove(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            activity?.let {
                UtilKotlin.showSnackMessage(it,successModel?.msg[0])
            }

//            productsAdapter.let {
//                it?.removeItemFromList(removePosition)
//            }
//            productsAdapter?.notifyDataSetChanged()

            //requestData()
            model.responseCodeDataSetter(null) // start details with this data please
            activity?.supportFragmentManager?.popBackStack()

        }

    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}