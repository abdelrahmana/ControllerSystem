package com.example.controllersystemapp.callcenter.delegate.order

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter
import com.example.controllersystemapp.accountant.delegatecallcenter.EditDelegateFragment
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.callcenter.delegate.DelegatePresenter
import com.example.controllersystemapp.callcenter.delegate.itemdetails.ItemDetailsFragment
import com.example.controllersystemapp.callcenter.delegate.model.DataBean
import com.example.controllersystemapp.callcenter.delegate.model.ItemListResponse
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import com.photonect.photographerapp.notificationphotographer.DonePackgae.ItemAdapter
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.orders_layout.*
import retrofit2.Response

class ItemFragment : Fragment(), OnRecyclerItemClickListener {

    lateinit var itemAdapter: ItemAdapter
    var itemList = ArrayList<DataBean>()

    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(this!!)!!

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

    private fun getDelegatesList() {
        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("order_id",arguments?.getInt(NameUtils.orderId,0)?:0)
            DelegatePresenter.getItemList(webService!!, ItemListObserver(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

      modelHandleChangeFragmentclass.notifyItemSelected?.observe(this!!, Observer { datamodel -> // position

            if (datamodel != null) {

                if (datamodel is  DataBean) {
                      // should call list of it
                    val bundle = Bundle()
                    bundle.putInt(NameUtils.itemId,datamodel.id?:0)
                    // should call item detalis
                    UtilKotlin.changeFragmentBack(activity!! ,
                        ItemDetailsFragment(),"itemDetails"  , bundle,
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

    var disposableObserver : DisposableObserver<Response<ItemListResponse>>?=null
    private fun ItemListObserver(): DisposableObserver<Response<ItemListResponse>> {

        disposableObserver= object : DisposableObserver<Response<ItemListResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<ItemListResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    itemList.clear()
                    itemList.addAll(response.body()?.data?:ArrayList())
                    getDeleagtesData()

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

        getDelegatesList()
    }

    private fun getDeleagtesData() {

    //    delegatesList.clear()
    /*    for (i in 0..4)
        {
            delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
        }*/

        itemAdapter = ItemAdapter(modelHandleChangeFragmentclass!! , itemList)
        delegatesRecycle?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = itemAdapter
        }


    }

   override fun onItemClick(position: Int) {
/*
      //  Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

        val bundle = Bundle()
      //  bundle.putInt(NameUtils.WHICHID,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
        // go to fragment of orders
        UtilKotlin.changeFragmentBack(activity!! ,
            DelegateDetailsFragment() , ""  , bundle,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
    }
*/
 /*   override fun delegateClickListener(position: Int) {
        selectedItemPosition = position
        val bottomSheetActions = BottomSheetActions()
        bottomSheetActions.show(activity?.supportFragmentManager!!, "bottomSheetActions")
*/
    }

}