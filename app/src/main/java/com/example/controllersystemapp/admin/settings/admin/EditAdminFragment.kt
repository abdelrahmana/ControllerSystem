package com.example.controllersystemapp.admin.settings.admin

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
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.example.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.example.controllersystemapp.admin.delegatesAccountants.models.EditAccountantRequest
import com.example.controllersystemapp.common.cities.CitesBottomSheet
import com.example.controllersystemapp.common.cities.Cities
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.Validation
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_edit_admin.*

class EditAdminFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var cityId: Int? = null
    var cityName: String? = null
    var adminId = 0
    var adminStatus : Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_edit_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmAddAdmin?.setOnClickListener {

            checkValidation()
        }

        backProfile?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        countryContainer?.setOnClickListener {
            val countriesCitesBottomSheet = CitesBottomSheet()
            //    countriesCitesBottomSheet.arguments = bundle
            countriesCitesBottomSheet.show(
                activity!!.supportFragmentManager,
                "countries_sheet"
            )
        }

        getAdminDetailsData()

        observeData()


    }

    private fun getAdminDetailsData() {

        val adminDetailsString = arguments?.getString(NameUtils.ADMIN_DETAILS)?:""
        val adminDetails = UtilKotlin.getAdminDetails(adminDetailsString)

        adminId = adminDetails?.data?.id?:0
        adminStatus = (adminDetails?.data?.status?:"").toInt()
        cityId = adminDetails?.data?.city?.id?:0
        adminNameEdt?.setText(adminDetails?.data?.name?:"")
        adminMobileEdt?.setText(adminDetails?.data?.phone?:"")
        countryNameEdt?.setText(adminDetails?.data?.city?.name?:"")
        adminEmailEdt?.setText(adminDetails?.data?.email?:"")



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
                    countryNameEdt?.setText(cityName ?: "")
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

        }



    }

//    private fun resetAllViews() {
//
//        adminNameEdt?.setText("")
//        adminMobileEdt?.setText("")
//        passwordTextEdt?.setText("")
//        confirmPasswordTextEdt?.setText("")
//        //roleId = null
//        cityId = null
//
//    }


    private fun checkValidation() {


        var errorMessage = ""

        if (adminNameEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.accountant_name_required)
            errorMessage += "\n"
        }


        if (adminMobileEdt?.text.isNullOrBlank()) {
            // phoneValid = false
            errorMessage += getString(R.string.phone_required)
            errorMessage += "\n"
        } else if (!Validation.validGlobalPhoneNumber(adminMobileEdt?.text.toString())) {
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

        if(adminEmailEdt?.text.isNullOrBlank()){
            errorMessage += getString(R.string.email_name_reuqired)
            errorMessage += "\n"
        }
        else if (!Validation.validateEmail(adminEmailEdt))
        {
            errorMessage += getString(R.string.email_invalid)
            errorMessage += "\n"
        }

//        if (passwordTextEdt.text.isNullOrBlank()) {
//            errorMessage += getString(R.string.password_required)
//            errorMessage += "\n"
//        }
//
//        if (confirmPasswordTextEdt.text.isNullOrBlank()) {
//            errorMessage += getString(R.string.confirm_pass_required)
//            errorMessage += "\n"
//        }

//        if (roleId == null) {
//            errorMessage += getString(R.string.job_required)
//            errorMessage += "\n"
//        }
//
//        if (!passwordTextEdt.text.isNullOrBlank() && !confirmPasswordTextEdt.text.isNullOrBlank()) {
//            if (confirmPasswordTextEdt.text.toString() != passwordTextEdt.text.toString()) {
//                errorMessage += getString(R.string.password_confirm)
//                errorMessage += "\n"
//            }
//
//        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            requestEditAdmin()

        }



    }

    private fun requestEditAdmin() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            val editAccountantRequest = EditAccountantRequest(
                adminId , adminNameEdt?.text?.toString() , adminMobileEdt?.text?.toString() ,
                cityId , adminEmailEdt?.text?.toString() , adminStatus
            )

            AdminPresenter.getEditAdmin(webService!!, editAccountantRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

}