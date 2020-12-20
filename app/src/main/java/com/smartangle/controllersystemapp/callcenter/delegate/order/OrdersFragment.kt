package com.smartangle.controllersystemapp.callcenter.delegate.order

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.controllersystemapp.callcenter.delegate.DelegatePresenter
import com.smartangle.controllersystemapp.callcenter.delegate.model.DelegateOrder
import com.smartangle.controllersystemapp.callcenter.delegate.model.OrderResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.OrderItemAdapter
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.orders_layout.*
import retrofit2.Response

class OrdersFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var orderAdapter: OrderItemAdapter
    var ordersList = ArrayList<DelegateOrder>()

    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!

        return inflater.inflate(R.layout.orders_layout, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
       // Log.d("back" , "Delegate crested")
        nameOfDelegates?.text = arguments?.getString(NameUtils.orderName,"")?:""

    }
    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    private fun getOrderList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("id",arguments?.getInt(NameUtils.delegateId,0)?:0)
            DelegatePresenter.getOrderList(webService!!, orderDispposible(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

      modelHandleChangeFragmentclass.notifyItemSelected?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {

                if (datamodel is  DelegateOrder) {

                    val bundle = Bundle()
                    bundle.putInt(NameUtils.orderId,datamodel.id?:0)
                    bundle.putString(NameUtils.orderName,datamodel.name?:"")
                    UtilKotlin.changeFragmentBack(activity!! ,
                        ItemFragment(),"order"  , bundle,
                        arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)


                }

                modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
            }

        })

    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        modelHandleChangeFragmentclass.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<OrderResponse>>?=null
    private fun orderDispposible(): DisposableObserver<Response<OrderResponse>> {

        disposableObserver= object : DisposableObserver<Response<OrderResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<OrderResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    ordersList.clear()
                    ordersList.addAll(response.body()?.data?.delegate_orders?:ArrayList())
                    getOrderAdapter()

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

    override fun onResume() {
        super.onResume()
        getOrderList()
    }

    private fun getOrderAdapter() {
        orderAdapter = OrderItemAdapter(modelHandleChangeFragmentclass!! , ordersList)
        delegatesRecycle?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = orderAdapter
        }


    }

    // implemented in view model
    override fun onItemClick(position: Int) {

      //  Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

      /*  val bundle = Bundle()
       bundle.putInt(NameUtils.orderId,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
        // go to fragment of orders
        UtilKotlin.changeFragmentBack(activity!! ,ItemFragment() , ""  , bundle,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
  */
    }

 /*   override fun delegateClickListener(position: Int) {
        selectedItemPosition = position
        val bottomSheetActions = BottomSheetActions()
        bottomSheetActions.show(activity?.supportFragmentManager!!, "bottomSheetActions")

    }*/

}