package com.example.controllersystemapp.admin.delegatesAccountants.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.example.controllersystemapp.bottomsheets.AdminBottomSheet
import com.example.controllersystemapp.bottomsheets.RoleBottomSheet
import com.example.controllersystemapp.common.AuthPresenter
import com.example.controllersystemapp.common.cities.CitesBottomSheet
import com.example.controllersystemapp.common.cities.Cities
import com.example.controllersystemapp.common.login.LoginRequest
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.NameUtils.WHICH_ADDED
import com.example.util.UtilKotlin
import com.example.util.Validation
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_accountant.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_products.*

class AddAccountantFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var cityId: Int? = null
    var cityName: String? = null
    var roleId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_add_accountant, container, false)
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



        observeData()
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

        if (accountantPasswordEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.password_required)
            errorMessage += "\n"
        }

        if (confirmPassEdt.text.isNullOrBlank()) {
            errorMessage += getString(R.string.confirm_pass_required)
            errorMessage += "\n"
        }

//        if (roleId == null) {
//            errorMessage += getString(R.string.job_required)
//            errorMessage += "\n"
//        }

        if (!accountantPasswordEdt.text.isNullOrBlank() && !confirmPassEdt.text.isNullOrBlank()) {
            if (confirmPassEdt.text.toString() != accountantPasswordEdt.text.toString()) {
                errorMessage += getString(R.string.password_confirm)
                errorMessage += "\n"
            }

        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {


            requestAddAccountant()

        }

    }

    private fun requestAddAccountant() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val addAccountantRequest = AddAccountantRequest(
                nameEdt?.text?.toString(),
                accountantPhoneEdt?.text?.toString(),
                accountantPasswordEdt?.text?.toString(),
                confirmPassEdt?.text?.toString(),
                2 , cityId
            )

            AccountantPresenter.getAddAccountant(webService!!, addAccountantRequest, activity!!, model)
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
                    successAdd(datamodel)
                }

                model.responseCodeDataSetter(null) // start details with this data please
            }

        })


//        model.intIdData?.observe(activity!!, Observer { datamodel ->
//
//            if (datamodel != null) {
//
//                Log.d("integerData", "observe ${datamodel}")
//                roleId = datamodel
//                when (roleId) {
//                    1 -> roleIdEdt?.setText(getString(R.string.choose_admin))
//                    2 -> roleIdEdt?.setText(getString(R.string.accounatnt))
//                    3 -> roleIdEdt?.setText(getString(R.string.callCenter))
//                    4 -> roleIdEdt?.setText(getString(R.string.delegate))
//
//                }
//                model.setInteDataVariable(null) // start details with this data please
//            }
//
//        })


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
            bundle.putString(WHICH_ADDED, msgtext?:"")

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

        nameEdt?.setText("")
        accountantPhoneEdt?.setText("")
        accountantPasswordEdt?.setText("")
        confirmPassEdt?.setText("")
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
            it?.intIdData?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}