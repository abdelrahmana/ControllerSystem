package com.example.controllersystemapp.accountant.makeorder

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.SCANERESULT
import com.example.controllersystemapp.admin.addproduct.ScanCodeActivity.Companion.scanCode
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_accountant_make_order.*

class AccountantMakeOrderFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var categoryID = 0
    var barCode : String ? = null


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

            val bundle = Bundle()
            bundle.putInt(NameUtils.WHICH_ADD_PRD_STORE, R.id.redirect_acc_fragments)

            UtilKotlin.changeFragmentBack(activity!! , FragmentProductclassification() , "productClassification"  ,
                bundle , R.id.redirect_acc_fragments)

        }

        scanOrderCard?.setOnClickListener {


            if (UtilKotlin.checkPermssionGrantedForImageAndFile(activity!!,
                    UtilKotlin.permissionScan,this)){
                // if the result ok go submit else on
                val intent = Intent(activity , ScanCodeActivity::class.java)
                startActivityForResult(intent  , scanCode)

            }
        }


        observeData()
    }

    override fun onResume() {
        super.onResume()
        barCodeTxt?.text = barCode


    }

    private fun observeData() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ViewModelHandleChangeFragmentclass.ProductClassification) {//when choose category return categoryID
                    Log.d("observeData", "dd $datamodel")
                    categoryID = datamodel.id?:-1
                    Log.d("finalText", " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}")
                    productClassificationTxt.text = " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                }
                model.responseCodeDataSetter(null) // start details with this data please
            }
            else{
                Log.d("testApi", "observeNull")

            }

        })

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
           // it?.errorMessage?.removeObservers(activity!!)
            it?.responseDataCode?.removeObservers(activity!!)
           // it?.notifyItemSelected?.removeObservers(activity!!)


        }
        super.onDestroyView()
    }
}