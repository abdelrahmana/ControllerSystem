package com.example.controllersystemapp.admin.storesproducts.adapters

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.controllersystemapp.R
import com.example.controllersystemapp.admin.storesproducts.StoresPresenter
import com.example.controllersystemapp.admin.storesproducts.fragments.StoresFragment
import com.example.controllersystemapp.admin.storesproducts.models.StoreDetailsResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import kotlinx.android.synthetic.main.fragment_store_details.*

class StoreDetailsFragment : Fragment() {


    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var storeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        storeId = arguments?.getInt(StoresFragment.STOREID)?:0

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_store_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backProdDetails?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


        deleteStoreBtn?.setOnClickListener {

            removeStoreItem()

        }

        observeData()

        getStoreDetailsData()
    }

    private fun removeStoreItem() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            StoresPresenter.deleteStorePresenter(webService!! ,
                storeId ,  activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun getStoreDetailsData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            StoresPresenter.getStoreDetails(webService!! ,
                storeId ,
                activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    private fun setStoreDetailsData(storeDetailsResponse: StoreDetailsResponse) {

        DetailsStoreNameTxt?.text = storeDetailsResponse?.data?.name?:""
        DetailsStoreLocationTxt?.text = storeDetailsResponse?.data?.name?:""
        DetailsAccNameTxt?.text = storeDetailsResponse?.data?.accountant?.name?:""

        setCategoryName(storeDetailsResponse)


    }

    private fun setCategoryName(storeDetailsResponse: StoreDetailsResponse) {

        if (storeDetailsResponse?.data?.categories?.isNullOrEmpty() == false)
        {
            var categoriesName = ""
            for (i in 0 until storeDetailsResponse?.data?.categories?.size!!) {
                categoriesName += storeDetailsResponse?.data?.categories?.get(i).name?:""
                if (i != storeDetailsResponse?.data?.categories?.size - 1)
                    categoriesName += " - "
            }

            DetailStoreCategory?.text = categoriesName
        }
        else{
            //empty
        }

    }

    private fun observeData() {
        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is StoreDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    setStoreDetailsData(datamodel)
                }

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

            //requestStoreData()
            model.responseCodeDataSetter(null) // start details with this data please
            activity?.supportFragmentManager?.popBackStack()

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