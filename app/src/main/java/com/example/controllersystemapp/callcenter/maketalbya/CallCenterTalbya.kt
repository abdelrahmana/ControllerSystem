package com.example.controllersystemapp.callcenter.maketalbya

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
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.SCANERESULT
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.scanCode
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassificationCenter
import com.example.controllersystemapp.callcenter.maketalbya.model.OrderCreateRequest
import com.example.controllersystemapp.callcenter.responsibledelegate.ResponsibleDelegateFragment
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_accountant_make_order.*
import okhttp3.MultipartBody
import retrofit2.Response


class CallCenterTalbya : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var categoryID = 0
    var quantity : String = "0"
    var barCode : String ? = null

var orderCreateRequest = OrderCreateRequest()
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

        observeData()

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
        chooseDelegate?.setOnClickListener{
            if (chooseDelegate?.isChecked==true) {
                if (delegateId == 0)
                    UtilKotlin.changeFragmentBack(
                        activity!!, ResponsibleDelegateFragment(), "Delegate_fragment",
                        null, R.id.frameLayout_direction
                    )
            }
            else
                delegateId = 0
        }
     /*   chooseDelegate?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                if (delegateId==0)
                UtilKotlin.changeFragmentBack(activity!! , ResponsibleDelegateFragment() , "Delegate_fragment"  ,
                    null ,R.id.frameLayout_direction)

            else {
                delegateId = 0 // remove delegate

            }
        }*/

        productClassificationCard?.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt(NameUtils.WHICH_ADD_PRD_STORE, /*R.id.redirect_acc_fragments*/
                arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
            bundle.putBoolean(FragmentProductclassification.ISCALLCENTER, /*R.id.redirect_acc_fragments*/
                true)
            UtilKotlin.changeFragmentBack(activity!! , FragmentProductclassificationCenter() , "productClassification"  ,
                bundle ,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction /*R.id.redirect_acc_fragments*/)

        }

        scanOrderCard?.setOnClickListener {
            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!,
                    UtilKotlin.permissionScan,this)){
                // if the result ok go submit else on
                val intent = Intent(activity , ScanCodeActivity::class.java)
                startActivityForResult(intent  , scanCode)

            }
        }

        makeOrderBtn?.setOnClickListener{
            postCallCenterTalbya()
        }
    }

    override fun onResume() {
        super.onResume()
        barCodeTxt?.text = barCode


    }

    private fun postCallCenterTalbya() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            if (UtilKotlin.checkAvalibalityOptions(recipientNameEdt?.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(mobileNumberEdt?.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(addressEdt?.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(emailAddressEdt?.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(makeOrderPriceEdt?.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(categoryID) == true
                && UtilKotlin.checkAvalibalityOptions(orderShoppingFeeEdt?.text.toString()) == true
            ) {
                clearProductList()
               setOrderRequest()

                progressDialog?.show()
                CategoriesPresenterCallCenter.setTalabyaPost(
                    webService!!,
                    ItemListObserver(),
                    orderCreateRequest!!
                )
            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.insert_all_data))

            }
        }else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
            }

    var delegateId = 0
    private fun setOrderRequest() {
        orderCreateRequest.products?.add(categoryID?:0)
        orderCreateRequest.quantity?.add(quantity.toInt())
        orderCreateRequest.address = addressEdt?.text.toString()
        orderCreateRequest.shipment_cost = orderShoppingFeeEdt?.text.toString()
        orderCreateRequest.delegate_id = 0
        orderCreateRequest.name = recipientNameEdt?.text.toString()
        orderCreateRequest.phone = mobileNumberEdt?.text.toString()
        orderCreateRequest.email = emailAddressEdt?.text.toString()

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
            .addFormDataPart("name", recipientNameEdt?.text.toString())
            .addFormDataPart("shipment_cost", orderShoppingFeeEdt?.text.toString())
            .addFormDataPart("address", addressEdt?.text.toString())
            .addFormDataPart("price", makeOrderPriceEdt?.text.toString())
            .addFormDataPart("phone", mobileNumberEdt?.text.toString())
            .addFormDataPart("email", emailAddressEdt?.text.toString())
            .addFormDataPart("delegate_id",delegateId.toString())
            .addFormDataPart("quantity[$0]",
                "1")
            .addFormDataPart("products[$0]",
                categoryID.toString())

    }

    private fun clearProductList() {
        orderCreateRequest.products?.clear()
        orderCreateRequest?.quantity?.clear()
    }


    override fun onDestroyView() {
        disposableObserver?.dispose()
        model.responseDataCode.removeObservers(activity!!)
        model.stringNameData.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<SuccessModel>>?=null
    private fun ItemListObserver(): DisposableObserver<Response<SuccessModel>> {

        disposableObserver= object : DisposableObserver<Response<SuccessModel>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<SuccessModel>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()

                    UtilKotlin.showSnackMessage(activity!!, response.body()?.msg?.get(0)?:"")
                    Handler().postDelayed({
                        activity?.onBackPressed()
                    },750)
                }
                else
                {
                    progressDialog?.dismiss()
                    if (response.errorBody() != null) {
                        // val error = PrefsUtil.handleResponseError(response.errorBody(), context!!)
                        val error = UtilKotlin.getErrorBodyResponse(response.errorBody(), context!!)
                        UtilKotlin.showSnackErrorInto(activity!!, error)
                    }

                }
            }
        }
        return disposableObserver!!
    }
    private fun observeData() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                if (datamodel is ViewModelHandleChangeFragmentclass.ProductClassification) {//when choose category return categoryID
                    categoryID = datamodel.id?:-1
                    quantity = datamodel.quantity?:"0"
                   // Log.d("finalText", " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}")
                    productClassificationTxt.text = " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                }
                model.responseCodeDataSetter(null) // start details with this data please
            }
            else{
                Log.d("testApi", "observeNull")

            }

        })

        model.stringNameData?.observe(activity!!, Observer { datamodel ->
            if (datamodel != null) {
                if (datamodel is ModelStringID) {
                    delegateId = datamodel.id?:0
                   // chooseDelegate?.isChecked = true
                }


                model.setStringData(null) // start details with this data please
            }


        })



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


}