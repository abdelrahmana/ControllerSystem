package com.example.controllersystemapp.admin.storesproducts.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.controllersystemapp.ModelStringID
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.categories.models.Data
import com.example.controllersystemapp.admin.delegatesAccountants.AccountantPresenter
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.AddAccountantFragment
import com.example.controllersystemapp.admin.delegatesAccountants.fragments.DoneDialogFragment
import com.example.controllersystemapp.admin.delegatesAccountants.models.AddAccountantRequest
import com.example.controllersystemapp.admin.productclassification.FragmentProductclassification
import com.example.controllersystemapp.admin.productclassification.productsubclassification.FragmentSubProductclassification
import com.example.controllersystemapp.admin.storesproducts.StoresPresenter
import com.example.controllersystemapp.admin.storesproducts.models.AddStoreRequest
import com.example.controllersystemapp.admin.storesproducts.models.NamesIdModel
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_add_accountant.*
import kotlinx.android.synthetic.main.fragment_add_store.*
import kotlinx.android.synthetic.main.fragment_add_store.confirmAddAccountantBtn


class AddStoreFragment : Fragment() {


    lateinit var model: ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog: Dialog

    lateinit var rootView: View
    var personName = ""
    var persomId = 0

    var categoryID: Int? = null
    var accountantID: Int? = null

    var categoriesList: ArrayList<Int>? = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_store, container, false)
        //model =UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
//        activity?.run {
//            model = ViewModelProviders.of(activity!!).get(ViewModelHandleChangeFragmentclass::class.java)
//        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        productCategoryContainer?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!, ProductCategoriesSelectFragment(), "productClassification",
                null, R.id.frameLayout_direction
            )
        }

        responsiblePersonContainer?.setOnClickListener {

            UtilKotlin.changeFragmentBack(
                activity!!,
                ResponsiblePersonFragment(),
                "ResponsiblePerson",
                null,
                R.id.frameLayout_direction
            )

        }

        backImgAddStore?.setOnClickListener {

            activity?.supportFragmentManager?.popBackStack()

        }

        increaseCountBtn?.setOnClickListener {

            val count = (counter?.text.toString()).toInt()
            counter?.text = (count + 1).toString()

        }

        decreaseCountBtn?.setOnClickListener {

            val count = (counter?.text.toString()).toInt()

            if (count > 0) {
                counter?.text = (count - 1).toString()

            } else {
                counter?.text = (0).toString()

            }


        }

        confirmAddAccountantBtn?.setOnClickListener {


            checkValidation()

        }

        Log.d("model", "create")
        Log.d("model", "createName $personName")

        model.stringNameData.observe(activity!!,
            Observer<ModelStringID> { modeStringId ->
                Log.d("model", "dataaa")

                if (modeStringId != null) {

                    personName = modeStringId.name
                    accountantID = modeStringId.id ?: -1
                    //setData(personName)
                    Log.d("model", "name ${modeStringId.name}")
                    Log.d("model", "id ${modeStringId.id}")
                }

            })

    }


    private fun checkValidation() {

        var errorMessage = ""

        if (storeNameEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.store_name_required)
            errorMessage += "\n"
        }

        if (storeAddressEdt?.text.isNullOrBlank()) {
            errorMessage += getString(R.string.store_address_required)
            errorMessage += "\n"
        }

        if (categoriesList?.size == 0) {
            errorMessage += getString(R.string.product_classified_required)
            errorMessage += "\n"
        }

        if (accountantID == null) {
            errorMessage += getString(R.string.responsible_person_required)
            errorMessage += "\n"
        }

        if (!errorMessage.isNullOrBlank()) {
            UtilKotlin.showSnackErrorInto(activity!!, errorMessage)
        } else {

            requestAddStore()

        }


    }

    private fun requestAddStore() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

          //  Log.d("categoriesSize", "${categoriesList?.size}")
            val addStoreRequest = AddStoreRequest(
                storeNameEdt?.text?.toString(),
                storeAddressEdt?.text?.toString(),
                accountantID,
                categoriesList
            )
            StoresPresenter.addStore(webService!!, addStoreRequest, activity!!, model)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun setData(personName: String) {
        Log.d("model", "nnnn")
        Log.d("model", "var $personName")


    }

    override fun onResume() {
        super.onResume()
        Log.d("model", "resume")

        observeCategry()

        responsiblePersonEditText?.setText(personName)
        model.setStringData(null)

    }

    private fun observeCategry() {

        model?.responseDataCode?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) {
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                    //model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
                    Log.d("notifyItem", "id ${modelSelected.id} name ${modelSelected.name}")
                    categoryID = modelSelected.id ?: -1
                    categoryProdTxt?.setText(modelSelected.name ?: "")


                }

                if (modelSelected is SuccessModel) {

                    progressDialog?.dismiss()
                    setSuccessAdd(modelSelected)

                }

                if (modelSelected is NamesIdModel) {

                    categoriesList?.clear()
                    var categoriesName = ""

                    for (i in 0 until modelSelected.idList?.size!!) {
                        categoriesList?.add(modelSelected.idList!![i])
                    }

                    for (i in 0 until modelSelected.namesList?.size!!) {
                        categoriesName += modelSelected.namesList[i]
                        if (i != modelSelected.namesList.size - 1)
                            categoriesName += ","
                    }
                    categoryProdTxt?.setText(categoriesName ?: "")

                }
                model?.responseCodeDataSetter(null) // remove listener please from here too and set it to null

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

    private fun setSuccessAdd(successModel: SuccessModel) {


        if (successModel?.msg?.isNullOrEmpty() == false) {
            var msgtext = ""
            for (i in successModel?.msg?.indices) {
                msgtext = msgtext + successModel?.msg?.get(i) + "\n"

            }

            UtilKotlin.showSnackMessage(activity, msgtext)
            //resetAllViews()
            Handler().postDelayed(Runnable {
                activity?.let {
                    it.supportFragmentManager.popBackStack()
                }
            }, 1000)

        }


    }

    private fun resetAllViews() {

        storeNameEdt?.setText("")
        storeAddressEdt?.setText("")
        categoryProdTxt?.setText("")
        categoriesList?.clear()
        responsiblePersonEditText?.setText("")
        accountantID = null


    }

    override fun onDestroyView() {
        model.let {
            it.stringNameData.removeObservers(activity!!)
            it.responseDataCode.removeObservers(activity!!)
            it.errorMessage.removeObservers(activity!!)


        }
        super.onDestroyView()

    }

}