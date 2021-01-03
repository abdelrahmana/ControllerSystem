package com.smartangle.controllersystemapp.admin.specialcustomers

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
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_clients_details.*


class ClientsDetailsFragment : Fragment() {

    var webService: WebService? = null
    lateinit var model: ViewModelHandleChangeFragmentclass
    lateinit var progressDialog : Dialog

    var clientId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        clientId = arguments?.getInt(AdminSpecicalCustomersragment.CLIENT_ID)?:0

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clients_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backClientDetails?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        deleteClientsBtn?.setOnClickListener {

            removeClient()

        }

        editClientsButton?.setOnClickListener {
            val bundle = Bundle()
            val clientsDetails = Gson().toJson(clientDetails)
            bundle.putString(NameUtils.ADMIN_CLIENTS_DETAILS , clientsDetails)
            UtilKotlin.changeFragmentBack(activity!! , EditClientFragment() , "EditClient"  ,
                bundle , R.id.frameLayout_direction)
        }
        observeData()

    }

    private fun removeClient() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ClientsPresenter.deleteClientPresenter(webService!! ,
                clientId , null , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }


    }

    override fun onResume() {
        super.onResume()

        getDetailsData()
    }

    private fun getDetailsData() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()

            ClientsPresenter.getClientDetails(webService!! ,
                clientId , activity!! , model)

        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }



    }

    var clientDetails : ClientDetailsResponse ? = null
    private fun observeData() {


        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                progressDialog?.hide()
                Log.d("testApi", "responseNotNull")

                if (datamodel is ClientDetailsResponse) {
                    Log.d("testApi", "isForyou")
                    getClientsDetsilsData(datamodel)
                    clientDetails = datamodel
                    editClientsButton?.isEnabled = true

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

    private fun getClientsDetsilsData(clientDetailsResponse: ClientDetailsResponse) {


        clientDetailsName?.text = clientDetailsResponse?.data?.name?:""
        clientPhoneName?.text = clientDetailsResponse?.data?.phone?:""
        clientDetailsLocation?.text = clientDetailsResponse?.data?.address?:""
        clientDetailsEmail?.text = clientDetailsResponse?.data?.email?:""
        receiptPrice?.text = clientDetailsResponse?.data?.receipt_amount?.toString()?:""
        receiptCurrancy?.text = clientDetailsResponse?.data?.currency?:""
        creditorCurrancy?.text = clientDetailsResponse?.data?.currency?:""
        creditorPrice?.text = clientDetailsResponse?.data?.creditor_amount?.toString()?:""
        totalPriceCustomer?.text = clientDetailsResponse?.data?.total_amount?.toString()?:""
        currancyCustomer?.text = clientDetailsResponse?.data?.currency?:""

//        clientDetailsImage?.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context!!,
//                    R.drawable.ic_img_placeholer_side
//                )
//                )

//        Glide.with(context!!).load(clientDetailsResponse?.data?.image?:"")
//            //.placeholder(R.drawable.image_delivery_item)
//            .dontAnimate().into(clientDetailsImage)



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

           // getCustomersData()
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