package com.smartangle.controllersystemapp.admin.settings.masrufat

import android.app.Dialog
import android.os.Bundle
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
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_fees_details.*

class FeesDetailsFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog: Dialog

    var expensesId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        expensesId = arguments?.getInt(
            MasrufatFragment.ADMIN_EXPENSES_ID, 0
        ) ?: 0
        return inflater.inflate(R.layout.fragment_fees_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageFees?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        rejectFeesBtn?.setOnClickListener {
            acceptOrRejectExpenses(2)

        }

        acceptFeesBtn?.setOnClickListener {

            acceptOrRejectExpenses(1)
        }

        observeData()

    }

    private fun acceptOrRejectExpenses(status: Int) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminExpensesPresenter.adminAcceptOrRejectExpenses(
                webService!!,
                expensesId,
                status,
                activity!!,
                model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    override fun onResume() {
        super.onResume()

        getDetailsData()
    }


    private fun observeData() {

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ExpensesDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    setAdminExpensesDetails(datamodel)
                }

                if (datamodel is SuccessModel) {
                    Log.d("testApi", "isForyou")
                    successData(datamodel)
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

    private fun successData(successModel: SuccessModel) {

        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            UtilKotlin.showSnackMessage(activity , successModel?.msg[0])

            model.responseCodeDataSetter(null)

            activity?.let {
                it.supportFragmentManager.popBackStack()
            }



//            Handler().postDelayed(Runnable {
//                activity?.let {
//                    it.supportFragmentManager.popBackStack()
//                }
//            }, 1000)
//
        }
    }

    private fun setAdminExpensesDetails(expensesDetailsResponse: ExpensesDetailsResponse) {

        expensesDetailsResponse?.data?.let { expensesDetailsData ->

            addressFeesData?.text = expensesDetailsData?.title ?: ""
            priceFeesData?.text = expensesDetailsData?.price ?: ""
            expensesDetailsText?.text = expensesDetailsData?.details ?: ""

        }

    }


    private fun getDetailsData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            AdminExpensesPresenter.getAdminExpensesDetails(
                webService!!, expensesId, activity!!, model
            )

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

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