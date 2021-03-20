package com.smartangle.controllersystemapp.admin.makeorders

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.smartangle.controllersystemapp.ModelStringID
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.makeorders.model.OrderCreateRequestAdmin
import com.smartangle.controllersystemapp.admin.productclassification.FragmentProductclassificationCenterAdmin
import com.smartangle.controllersystemapp.admin.specialcustomers.AdminSpecicalCustomersragment
import com.smartangle.controllersystemapp.admin.specialcustomers.ClientsData
import com.smartangle.controllersystemapp.admin.storesproducts.fragments.StoresFragment
import com.smartangle.controllersystemapp.admin.storesproducts.models.StoresData
import com.smartangle.controllersystemapp.callcenter.responsibledelegate.ResponsibleDelegateFragment
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.SuccessModelV2
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_admin_make_order.*
import kotlinx.android.synthetic.main.fragment_admin_make_order.productClassificationTxt
import retrofit2.Response


class AdminMakeOrderFragment : Fragment() {

    lateinit var model : ViewModelHandleChangeFragmentclass
    var webService: WebService? = null
    lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        webService = ApiManagerDefault(context!!).apiService
        model = UtilKotlin.declarViewModel(activity)!!
        progressDialog = UtilKotlin.ProgressDialog(context!!)

        return inflater.inflate(R.layout.fragment_admin_make_order, container, false)
    }
    var categoryID = 0
    var quantity : String = "0"
    var delegateId : Int = 0

    var orderCreateRequestAdmin = OrderCreateRequestAdmin()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backImageOrder?.setOnClickListener {
            if (activity?.supportFragmentManager?.backStackEntryCount == 1)
            {
                activity?.finish()
            }
            else{
                activity?.supportFragmentManager?.popBackStack()
            }
        }


        creditorCheckImg?.setOnClickListener {
            creditorCheckImg?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_radio_check))
            cashCheckImg?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_radio_not_check))
            valuePaidContainer?.visibility = View.VISIBLE
        }

        cashCheckImg?.setOnClickListener {
            cashCheckImg?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_radio_check))
            creditorCheckImg?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_radio_not_check))
            valuePaidContainer?.visibility = View.GONE
        }

        productCard?.setOnClickListener {
            if (UtilKotlin.checkAvalibalityOptions(orderCreateRequestAdmin?.warehouse_id?:0)==true) {
                val bundle = Bundle()
                bundle.putInt(
                    NameUtils.WHICH_ADD_PRD_STORE, /*R.id.redirect_acc_fragments*/
                    arguments?.getInt(NameUtils.WHICHID, R.id.frameLayout_direction)
                        ?: R.id.frameLayout_direction
                )

                bundle.putInt(
                    WARE_HOUSE, /*R.id.redirect_acc_fragments*/
                    wareHouseLimit)

                bundle.putInt(
                    NameUtils.WHICH_ADD_PRD_STORE, /*R.id.redirect_acc_fragments*/
                    arguments?.getInt(NameUtils.WHICHID, R.id.frameLayout_direction)
                        ?: R.id.frameLayout_direction
                )
                /*   bundle.putBoolean(
                FragmentProductclassification.ISCALLCENTER, /*R.id.redirect_acc_fragments*/
                true)*/
                model?.notifyItemSelected?.removeObservers(activity!!)
                UtilKotlin.changeFragmentBack(
                    activity!!,
                    FragmentProductclassificationCenterAdmin(),
                    "admin_fragment",
                    bundle,
                    arguments?.getInt(NameUtils.WHICHID, R.id.frameLayout_direction)
                        ?: R.id.frameLayout_direction /*R.id.redirect_acc_fragments*/
                )
            } else {
                UtilKotlin.showSnackErrorInto(activity!!,getString(R.string.warehouse_needed_first))
            }
        }
        customerSelectCard?.setOnClickListener {

            UtilKotlin.changeFragmentBack(activity!! , AdminSpecicalCustomersragment() , "make_order"  , Bundle().also {
                it.putBoolean(NameUtils.FORADMINORDER,true)
            },R.id.frameLayout_direction)


        }
        storeCard?.setOnClickListener{
            UtilKotlin.changeFragmentBack(activity!! ,StoresFragment() , "make_order"  , Bundle().also {
                it.putBoolean(NameUtils.FORADMINORDER,true)
            },R.id.frameLayout_direction)
        }
        selectDelegate?.setOnClickListener{
            if (selectDelegate?.isChecked==true) {
                if (delegateId == 0)
                    UtilKotlin.changeFragmentBack(
                        activity!!, ResponsibleDelegateFragment(), "Delegate_fragment",
                        null, R.id.frameLayout_direction
                    )
            }
            else
                delegateId = 0
        }
        setObserversLisetner()
        confirmMakeOrderBtn?.setOnClickListener{
            postTalbyaAdmin()
        }
    }
    private fun postTalbyaAdmin() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            if (UtilKotlin.checkAvalibalityOptions(orderCreateRequestAdmin.client_id?:0) == true
                && UtilKotlin.checkAvalibalityOptions(orderCreateRequestAdmin.products?:ArrayList<Int>()) == true
                && UtilKotlin.checkAvalibalityOptions(orderCreateRequestAdmin.quantity?:ArrayList<Int>()) == true
                && UtilKotlin.checkAvalibalityOptions(shoppingFeeEdt.text.toString()) == true
                && UtilKotlin.checkAvalibalityOptions(orderCreateRequestAdmin.warehouse_id?:0) == true

            ) {
                orderCreateRequestAdmin.shipment_cost = shoppingFeeEdt?.text.toString()
                orderCreateRequestAdmin.delegate_id =delegateId
                progressDialog?.show()
                PresenterMakeOrder.setTalabyaPost(
                    webService!!,
                    ItemListObserver(),
                    orderCreateRequestAdmin
                )
            } else {
                UtilKotlin.showSnackErrorInto(activity, getString(R.string.insert_all_data))

            }
        }else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }
    var wareHouseLimit = 0 // wareHouse quantity to set limitation on product

    private fun setObserversLisetner() {
        model.stringNameData.observe(activity!! ,
            Observer<ModelStringID> { modeStringId ->
                Log.d("modelOrder" , "dataaa")

                if (modeStringId !=null) {

                    //setData(personName)
                    delegateId = modeStringId.id?:0
              //      model.setStringData(null)

                }

            })

        model.notifyItemSelectedClient.observe(activity!! ,
            Observer<Any> { modeStringId ->

                if (modeStringId !=null) {
                    if (modeStringId is ClientsData) {
                        clientId?.text =(modeStringId?.name?:"")
                        orderCreateRequestAdmin.client_id = modeStringId.id?:0
                    }

                //    model.setNotifyItemSelectedClient(null)

                }

            })
        model.notifyStoreFragment.observe(activity!! ,
            Observer<Any> { modeStringId ->
                Log.d("modelOrder" , "dataaa")

                if (modeStringId !=null) {

                    if (modeStringId is StoresData) {
                        warehouse?.append(" ("+(modeStringId?.name?:"")+")")
                            wareHouseLimit = modeStringId.quantity?:0

                        orderCreateRequestAdmin.warehouse_id = modeStringId.id?:0
                    }
           //         model.setNotifyItemSelected(null)

                }

            })

        model.responseDataCode?.observe(activity!!, Observer { datamodel ->
            Log.d("testApi", "observe")

            if (datamodel != null) {
                if (datamodel is ViewModelHandleChangeFragmentclass.ProductClassification) {//when choose category return categoryID
                    categoryID = datamodel.id?:-1
                    quantity = datamodel.quantity?:"0"
                    orderCreateRequestAdmin.products?.clear()
                    orderCreateRequestAdmin.quantity?.clear()
                    orderCreateRequestAdmin.products?.add(categoryID)
                    orderCreateRequestAdmin.quantity?.add(quantity.toInt())
           //         orderCreateRequestAdmin.products[0]= categoryID
            //        orderCreateRequestAdmin.quantity[0]= quantity.toInt()

                            // Log.d("finalText", " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}")
                    productClassificationTxt.text = " ${datamodel.parentName} - ${datamodel.subParentName} - ${datamodel.lastSubParentName}"
                }
         //       model.responseCodeDataSetter(null) // start details with this data please
            }
            else{

            }

        })
    }

    override fun onResume() {
        super.onResume()
       // responsiblePersonEditText?.setText(personName)

    }

    var disposableObserver : DisposableObserver<Response<SuccessModelV2>>?=null
    private fun ItemListObserver(): DisposableObserver<Response<SuccessModelV2>> {

        disposableObserver= object : DisposableObserver<Response<SuccessModelV2>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<SuccessModelV2>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    model.stringNameData.removeObservers(activity!!)
                    model.notifyItemSelectedClient.removeObservers(activity!!)
                    model.notifyStoreFragment.removeObservers(activity!!)
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
    override fun onDestroyView() {
        model.let {
           it.stringNameData.removeObservers(activity!!)
           it.notifyItemSelectedClient.removeObservers(activity!!)
           it.notifyStoreFragment.removeObservers(activity!!)

        }
        disposableObserver?.dispose()
        super.onDestroyView()

    }
    companion object {
        val WARE_HOUSE = "ware_id"
    }
}