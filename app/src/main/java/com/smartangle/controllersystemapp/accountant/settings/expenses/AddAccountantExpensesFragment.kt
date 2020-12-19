package com.smartangle.controllersystemapp.accountant.settings.expenses

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
import kotlinx.android.synthetic.main.fragment_add_accountant_expenses.*

class AddAccountantExpensesFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog


    var expensesID = -1
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

        return inflater.inflate(R.layout.fragment_add_accountant_expenses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expensesID = arguments?.getInt(ExpensesFragment.EXPENSES_ID , -1) ?: -1

        backImageAddPayment?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


        sendAddExpensesBtn?.setOnClickListener {

            if (checkValidData())
            {
                if (expensesID  != -1)
                {
                    //update
                    updateExpensesData()


                }
                else{
                    //add
                    sendExpensesData()

                }

            }

        }


        handelUpdateOrAdd()

        observeCreateExpesnse()
    }

    private fun updateExpensesData() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ExpensesPresenter.updateExpensesData(webService!! ,
                expensesTitleEdt?.text?.toString()?:"",
                addExpenseDescriptionEdt?.text?.toString()?:"",
                expensesPriceEdt?.text?.toString()?:"",
                expensesID ,
                activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    }

    private fun handelUpdateOrAdd() {

        if (expensesID  != -1)
        {
            //update

            sendAddExpensesBtn?.text = getString(R.string.update)
            expensesTitleEdt?.setText(arguments?.getString(ExpensesFragment.EXPENSES_TITLE)?:"")
            expensesPriceEdt?.setText(arguments?.getString(ExpensesFragment.EXPENSES_PRICE)?:"")
            addExpenseDescriptionEdt?.setText(arguments?.getString(ExpensesFragment.EXPENSES_DESC)?:"")
           // getExpenseDetails()


        }
        else{
            //add
            sendAddExpensesBtn?.text = getString(R.string.send)

        }


    }

    private fun getExpenseDetails() {


        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ExpensesPresenter.expensesDetailsData(webService!! ,expensesID , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    private fun observeCreateExpesnse() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is SuccessModel) {

                    model.responseCodeDataSetter(null) // start details with this data please
                    successAdd(datamodel)
                }

//                if (datamodel is AccountantExpensesDetailsResponse) {
//                    Log.d("testApi", "isForyou")
//                    detailsResponse(datamodel)
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

    private fun detailsResponse(detailsResponse: AccountantExpensesDetailsResponse) {


        detailsResponse?.data?.let {dataExpensesDetails ->

            expensesTitleEdt?.setText(dataExpensesDetails?.title?:"")
            expensesPriceEdt?.setText(dataExpensesDetails?.price?:"")
            addExpenseDescriptionEdt?.setText(dataExpensesDetails?.details?:"")


        }



    }

    private fun successAdd(successModel: SuccessModel) {


        if (successModel?.msg?.isNullOrEmpty() == false)
        {
            UtilKotlin.showSnackMessage(activity , successModel?.msg[0])

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

    private fun sendExpensesData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ExpensesPresenter.addExpensesData(webService!! ,
                expensesTitleEdt?.text?.toString()?:"",
                addExpenseDescriptionEdt?.text?.toString()?:"",
                expensesPriceEdt?.text?.toString()?:"",
                activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun checkValidData() : Boolean{

        var errorMessage = ""

        if (expensesTitleEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.title_required)
            errorMessage += "\n"
        }

        if (expensesPriceEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.price_required)
            errorMessage += "\n"
        }

        if (addExpenseDescriptionEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.description_required)
            errorMessage += "\n"
        }

        return if (errorMessage.isNullOrBlank()) {
            true
        } else {
            UtilKotlin.showSnackErrorInto(activity, errorMessage)
            false
        }


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        model.let {
            it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)

        }
        super.onDestroyView()
    }
}