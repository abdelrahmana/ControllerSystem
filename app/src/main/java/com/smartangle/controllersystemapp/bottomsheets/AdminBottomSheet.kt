package com.smartangle.controllersystemapp.bottomsheets

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.settings.admin.AdminPresenter
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils.ADMIN_ID
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_admin_bottom_sheet.*


class AdminBottomSheet : BottomSheetDialogFragment() {


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

        return inflater.inflate(R.layout.fragment_admin_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // MyUtils.setCorners(view = lang_card!!, topLeft = 60f, topRight = 60f)

        deleteAdminText?.setOnClickListener{

            remveAdmin()
            //dismiss()
        }
        blockAdminText?.setOnClickListener{
            dismiss()
        }
        closeSheet?.setOnClickListener{
            dismiss()

        }

        observeData()
    }

    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.dismiss()
                dismiss()
                Log.d("testApi", "responseNotNull")

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

            dismiss()

        }

    }

    private fun remveAdmin() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminPresenter.deleteAdminPresenter(webService!! ,
                arguments?.getInt(ADMIN_ID)?:-1 ,  activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onDismiss(dialog: DialogInterface) {

        model.setStringVar("remove")

        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): AdminBottomSheet {
            return AdminBottomSheet()
        }
    }





}