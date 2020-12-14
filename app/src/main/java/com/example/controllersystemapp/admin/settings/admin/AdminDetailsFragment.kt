package com.example.controllersystemapp.admin.settings.admin

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.EditAccountantFragment
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantDetailsResponse
import com.example.controllersystemapp.admin.settings.admin.AdminFragment.Companion.ADMINID
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_admin_details.*

class AdminDetailsFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImgAdminDetails?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        editAdminButton?.setOnClickListener {

            val bundle = Bundle()
            val adminDetailsData = Gson().toJson(adminDetails)
            bundle.putString(NameUtils.ADMIN_DETAILS , adminDetailsData)
            UtilKotlin.changeFragmentBack(activity!! , EditAdminFragment() , "EditAdmin"  ,
                bundle , R.id.frameLayout_direction)
        }

        observeData()

    }



    override fun onResume() {
        super.onResume()

        getAdminDetailsRequest()
    }

    private fun getAdminDetailsRequest() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminPresenter.getAdminDetails(webService!! ,
                arguments?.getInt(ADMINID)?:0 , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    var adminDetails : AdminDetailsResponse ? = null
    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is AdminDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getAdminData(datamodel)
                    adminDetails = datamodel
                    editAdminButton?.isEnabled = true
                }

//                if (datamodel is SuccessModel) {
//                    Log.d("testApi", "isForyou")
//                    successRemove(datamodel)
//                }

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

    private fun getAdminData(adminData: AdminDetailsResponse) {


        adminDetailsName?.text = adminData?.data?.name?:""
        adminPhoneName?.text = adminData?.data?.phone?:""
        adminDetailsLocation?.text = adminData?.data?.city?.name?:""
        adminDetailsEmail?.text = adminData?.data?.email?:""
        Glide.with(context!!).load(adminData?.data?.image?:"")
            //.placeholder(R.drawable.image_delivery_item)
            .dontAnimate().into(adminDetailsImage)


    }


    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}