package com.smartangle.controllersystemapp.accountant.makeorder.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.smartangle.controllersystemapp.accountant.makeorder.AccountantMakeOrderPresenter
import com.smartangle.controllersystemapp.accountant.makeorder.models.AccountantMakeOrderRequest
import com.smartangle.controllersystemapp.accountant.products.models.Data
import com.smartangle.controllersystemapp.admin.addproduct.ScanCodeActivity
import com.smartangle.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.SCANERESULT
import com.smartangle.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.scanCode
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.PrefsUtil
import com.smartangle.util.UtilKotlin
import com.smartangle.util.Validation
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant_make_order.*


class AccountantMakeOrderFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var categoryID = 0
    var barCode : String ? = null
    var productName : String ? = null
    var delegateName : String ? = null

    var productsList = ArrayList<Int>()
    var quantityList = ArrayList<Int>()

    var delegateId : Int ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_accountant_make_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        backOrderImg?.setOnClickListener {

            activity?.let {
                if (it.supportFragmentManager.backStackEntryCount == 1)
                {
                    it.finish()
                }
                else{
                    it.supportFragmentManager.popBackStack()
                }
            }

        }

        productClassificationCard?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!, AccountantCategoriesFragment(), "AccountantCategoriesFragment",
                null , R.id.redirect_acc_fragments
            )

//            val bundle = Bundle()
//            bundle.putInt(NameUtils.WHICH_ADD_PRD_STORE, /*R.id.redirect_acc_fragments*/
//                arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
//
//            UtilKotlin.changeFragmentBack(activity!! , FragmentProductclassification() , "productClassification"  ,
//                bundle ,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction /*R.id.redirect_acc_fragments*/)

        }

        scanOrderCard?.setOnClickListener {


            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!,
                    UtilKotlin.permissionScan,this)){
                // if the result ok go submit else on
                val intent = Intent(activity , ScanCodeActivity::class.java)
                startActivityForResult(intent  , scanCode)

            }
        }

        chooseDelegate.setOnClickListener(View.OnClickListener { view ->
            if ((view as CompoundButton).isChecked) {
                UtilKotlin.changeFragmentBack(
                    activity!!, AccDelegatesFragment(), "AccDelegatesFragment",
                    null , R.id.redirect_acc_fragments
                )
            } else {
                Log.d("cjeckData" , "false")
                delegateId = null
                delegateSelectedName?.text = ""
                delegateSelectedName?.visibility = View.GONE
            }
        })

        makeOrderBtn?.setOnClickListener {

            if (checkValidData()) {

                if (delegateId == null)
                {
                    delegateId = PrefsUtil.getUserModel(context!!)?.id ?: 0
                }
                val accountantMakeOrderRequest = AccountantMakeOrderRequest(
                    recipientNameEdt?.text?.toString(),
                    emailAddressEdt?.text?.toString(), mobileNumberEdt?.text?.toString(),
                    addressEdt?.text?.toString(), orderShoppingFeeEdt?.text?.toString(),
                    delegateId, productsList, quantityList
                )
                requestMakeOrder(accountantMakeOrderRequest)
            }


        }

        observeData()
    }

    private fun requestMakeOrder(accountantMakeOrderRequest: AccountantMakeOrderRequest) {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AccountantMakeOrderPresenter.accountantCreateOrder(
                webService!!, accountantMakeOrderRequest
                , activity!!, model
            )


        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun checkValidData(): Boolean {

        var errorMessage = ""
        if (recipientNameEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.name_is_required)
            errorMessage += "\n"
        }

        if (mobileNumberEdt?.text.isNullOrBlank()) {
            // phoneValid = false
            errorMessage += getString(R.string.phone_required)
            errorMessage += "\n"
        } else if (!Validation.validGlobalPhoneNumber(mobileNumberEdt?.text.toString())) {
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


        if (emailAddressEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.email_name_reuqired)
            errorMessage += "\n"
        } else if (!Validation.validateEmail(emailAddressEdt)) {
            errorMessage += getString(R.string.email_invalid)
            errorMessage += "\n"
        }

        if (orderShoppingFeeEdt?.text.isNullOrBlank()) {
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

    override fun onResume() {
        super.onResume()

        chooseDelegate?.isChecked = delegateId != null

        barCodeTxt?.text = barCode
        delegateSelectedName?.visibility = View.VISIBLE
        delegateSelectedName?.text = delegateName
        productClassificationTxt?.text = productName

    }

    private fun observeData() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testObserve", "inside")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testObserve", "notNull")
//                if (datamodel is ViewModelHandleChangeFragmentclass.ProductClassification) {//when choose category return categoryID
//                    Log.d("observeData", "dd $datamodel")
//                    categoryID = datamodel.id?:-1
//                    Log.d("finalText", " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}")
//                    productClassificationTxt.text = " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
//                }

                if (datamodel is Data)
                {
                    Log.d("testObserve", "data")

                    getSelectedProducts(datamodel)
                }
                if (datamodel is CallCenterDelegateData)
                {
                    Log.d("testObserve", "delegate ${datamodel?.id?:0}")
                    delegateId = datamodel?.id?:0
                    delegateName = datamodel?.name?:""
                    delegateSelectedName?.visibility = View.VISIBLE
                    delegateSelectedName?.text = delegateName
                }
                if (datamodel is SuccessModel) {
                    getSuccessData(datamodel)
                }
                model.responseCodeDataSetter(null) // start details with this data please
            }
            else{
                Log.d("testObserve", "nulll")

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

//        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
//            if (modelSelected != null) { // if null here so it's new service with no any data
//                Log.d("paretnId" , "observeParent")
//
//                if (modelSelected is CallCenterDelegateData) {
//
//                    delegateId = modelSelected?.id?:0
//                    delegateSelectedName?.visibility = View.VISIBLE
//                    delegateSelectedName?.text = modelSelected?.name?:""
//
//                }
//                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
//
//            }
//
//            Log.d("paretnId" , "j22j")
//        })

//        model.stringDataVar?.observe(viewLifecycleOwner, Observer { datamodel ->
//            Log.d("testApi", "observe")
//
//            if (datamodel != null) {
//                progressDialog?.hide()
//                Log.d("testApi", "responseNotNull")
//                Log.d("resultDataObserve", datamodel) // Prints scan results
//                barCode = datamodel
//                barCodeTxt?.text = barCode
//
//                model.setStringVar(null) // start details with this data please
//            }
//            else{
//                Log.d("testApi", "observeNull")
//
//            }
//
//        })



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

    private fun getSelectedProducts(datamodel: Data) {

        productsList.clear()
        quantityList.clear()
        productsList?.add(0 , datamodel?.id?:0)
        quantityList.add(0 , datamodel?.selectedQuantity?:1)
        productName = datamodel?.name?:""
        productClassificationTxt?.text = productName
        Log.d("finalSelectedProd" , "id ${datamodel?.id?:0}")
        Log.d("finalSelectedProd" , "quantity ${datamodel?.selectedQuantity?:1}")

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == scanCode) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.getStringExtra(SCANERESULT)
                Log.d("resultData", result) // Prints scan results
                barCode = result
                barCodeTxt?.text = result
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    } //onActivityResult

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

         if (requestCode == UtilKotlin.permissionScan) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(activity , ScanCodeActivity::class.java)
                startActivityForResult(intent  , ScanCodeActivity.scanCode)

            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.cant_add_image))

            }
        }
    }


    override fun onDestroyView() {
        model.let {
            Log.d("destroyCrete", "true")

             it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
            it?.notifyItemSelected?.removeObservers(activity!!)


        }
        super.onDestroyView()
    }
}