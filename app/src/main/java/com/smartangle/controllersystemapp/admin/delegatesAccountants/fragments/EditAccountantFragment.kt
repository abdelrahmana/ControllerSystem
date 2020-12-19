package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments

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
import com.smartangle.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.EditAccountantRequest
import com.smartangle.controllersystemapp.common.cities.CitesBottomSheet
import com.smartangle.controllersystemapp.common.cities.Cities
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.Validation
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_edit_accountant.*


class EditAccountantFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var cityId: Int? = null
    var cityName: String? = null

    var accountantId : Int = 0
    var accountantStatus : Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_edit_accountant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backImgAddAccountant?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        confirmAddAccountantBtn?.setOnClickListener {

            checkValidation()

        }

        countryNameContainer?.setOnClickListener {
            val countriesCitesBottomSheet = CitesBottomSheet()
            //    countriesCitesBottomSheet.arguments = bundle
            countriesCitesBottomSheet.show(
                activity!!.supportFragmentManager,
                "countries_sheet"
            )
        }

//        roleIdContainer.setOnClickListener {
//
//            val roleBottomSheet = RoleBottomSheet.newInstance()
//            roleBottomSheet.show(activity?.supportFragmentManager!!, RoleBottomSheet.TAG)
//
//        }


        setAccountantData()

        observeData()
    }

    private fun setAccountantData() {

        val accountantDetailsString = arguments?.getString(NameUtils.ACCOUNTANT_ADMIN_DETAILS)?:""
        val accountantDetails = UtilKotlin.getAccountantDetails(accountantDetailsString)

        accountantId = accountantDetails?.data?.id?:0
        accountantStatus = (accountantDetails?.data?.status?:"").toInt()
        cityId = accountantDetails?.data?.city?.id?:0
        nameEdt?.setText(accountantDetails?.data?.name?:"")
        accountantPhoneEdt?.setText(accountantDetails?.data?.phone?:"")
        cityEditText?.setText(accountantDetails?.data?.city?.name?:"")
        accountantEmailEdt?.setText(accountantDetails?.data?.email?:"")

    }

    private fun checkValidation() {

        var errorMessage = ""

        if (nameEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.accountant_name_required)
            errorMessage += "\n"
        }


        if (accountantPhoneEdt?.text.isNullOrBlank()) {
            // phoneValid = false
            errorMessage += getString(R.string.phone_required)
            errorMessage += "\n"
        } else if (!Validation.validGlobalPhoneNumber(accountantPhoneEdt?.text.toString())) {
            //phoneValid = false
            errorMessage += getString(R.string.phone_not_valid)
            errorMessage += "\n"
        } else {
            // phoneValid = true
        }

        if (cityId == null) {
            errorMessage += getString(R.string.city_name_required)
            errorMessage += "\n"
        }

        if(accountantEmailEdt?.text.isNullOrBlank()){
            errorMessage += getString(R.string.email_name_reuqired)
            errorMessage += "\n"
        }
        else if (!Validation.validateEmail(accountantEmailEdt))
        {
            errorMessage += getString(R.string.email_invalid)
            errorMessage += "\n"
        }

//        if (accountantPasswordEdt.text.isNullOrBlank()) {
//            errorMessage += getString(R.string.password_required)
//            errorMessage += "\n"
//        }
//
//        if (confirmPassEdt.text.isNullOrBlank()) {
//            errorMessage += getString(R.string.confirm_pass_required)
//            errorMessage += "\n"
//        }

//        if (roleId == null) {
//            errorMessage += getString(R.string.job_required)
//            errorMessage += "\n"
//        }

//        if (!accountantPasswordEdt.text.isNullOrBlank() && !confirmPassEdt.text.isNullOrBlank()) {
//            if (confirmPassEdt.text.toString() != accountantPasswordEdt.text.toString()) {
//                errorMessage += getString(R.string.password_confirm)
//                errorMessage += "\n"
//            }
//
//        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            requestEditAccountant()

        }

    }

    private fun requestEditAccountant() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            val editAccountantRequest = EditAccountantRequest(

                accountantId , nameEdt?.text?.toString() , accountantPhoneEdt?.text?.toString() ,
                cityId , accountantEmailEdt?.text?.toString() ,accountantStatus
            )
            AccountantPresenter.getEditAccountant(webService!!, editAccountantRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }




    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is Cities) {
                    Log.d("TAG", "addAccount")
                    Log.d("TAG", "id ${datamodel.id} name ${datamodel.name}")

                    cityId = datamodel.id // city id
                    cityName = datamodel.name ?: ""
                    cityEditText?.setText(cityName ?: "")
                }

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


//            resetAllViews()
//            val bundle = Bundle()
//            bundle.putString(NameUtils.WHICH_ADDED, msgtext?:"")
//
//            UtilKotlin.changeFragmentBack(
//                activity!!,
//                DoneDialogFragment(),
//                "DoneAddAccountant",
//                bundle,
//                R.id.frameLayout_direction
//            )
        }


    }

    private fun resetAllViews() {

        nameEdt?.setText("")
        accountantPhoneEdt?.setText("")
//        accountantPasswordEdt?.setText("")
//        confirmPassEdt?.setText("")
        //roleId = null
        cityId = null

    }


//    override fun onResume() {
//        super.onResume()
//
//        cityEditText?.setText(cityName?:"")
//        model.responseCodeDataSetter(null) // start details with this data please
//    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}