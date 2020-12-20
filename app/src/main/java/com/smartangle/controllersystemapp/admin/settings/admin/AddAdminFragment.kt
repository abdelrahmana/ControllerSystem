package com.smartangle.controllersystemapp.admin.settings.admin

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.smartangle.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.smartangle.controllersystemapp.common.cities.CitesBottomSheet
import com.smartangle.controllersystemapp.common.cities.Cities
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.NameUtils.ADMIN_ROLE_ID
import com.smartangle.util.UtilKotlin
import com.smartangle.util.Validation
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_admin.*


class AddAdminFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var cityId: Int? = null
    var cityName: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_add_admin, container, false)
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

        observeData()


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
            val bundle = Bundle()
            bundle.putString(NameUtils.WHICH_ADDED, msgtext?:"")

            UtilKotlin.changeFragmentBack(
                activity!!,
                DoneDialogFragment(),
                "DoneAddAccountant",
                bundle,
                R.id.frameLayout_direction
            )

        }



    }

    private fun resetAllViews() {

        adminNameEdt?.setText("")
        adminMobileEdt?.setText("")
        passwordTextEdt?.setText("")
        confirmPasswordTextEdt?.setText("")
        //roleId = null
        cityId = null

    }


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

        if (passwordTextEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.password_required)
            errorMessage += "\n"
        }

        if (confirmPasswordTextEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.confirm_pass_required)
            errorMessage += "\n"
        }

//        if (roleId == null) {
//            errorMessage += getString(R.string.job_required)
//            errorMessage += "\n"
//        }

        if (!passwordTextEdt.text.isNullOrBlank() && !confirmPasswordTextEdt.text.isNullOrBlank()) {
            if (confirmPasswordTextEdt.text.toString() != passwordTextEdt.text.toString()) {
                errorMessage += getString(R.string.password_confirm)
                errorMessage += "\n"
            }

        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            requestAddAdmin()

        }



    }

    private fun requestAddAdmin() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val addAccountantRequest = AddAccountantRequest(
                adminNameEdt?.text?.toString(),
                adminMobileEdt?.text?.toString(),
                passwordTextEdt?.text?.toString(),
                confirmPasswordTextEdt?.text?.toString(),
                ADMIN_ROLE_ID , cityId
            )

            AdminPresenter.getAddAdmin(webService!!, addAccountantRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }
}