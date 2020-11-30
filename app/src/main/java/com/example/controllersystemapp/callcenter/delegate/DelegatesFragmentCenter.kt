package com.example.controllersystemapp.callcenter.delegate

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.controllersystemapp.R
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterDelegateData
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.example.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.example.controllersystemapp.callcenter.delegate.model.DelegateResponse
import com.example.controllersystemapp.callcenter.delegate.order.OrdersFragment
import com.example.util.ApiConfiguration.ApiManagerDefault
import com.example.util.ApiConfiguration.WebService
import com.example.util.NameUtils
import com.example.util.NestedScrollPaginationView
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_delegates.*
import kotlinx.android.synthetic.main.search_layout.*
import retrofit2.Response

class DelegatesFragmentCenter : Fragment(), OnRecyclerItemClickListener, NestedScrollPaginationView.OnMyScrollChangeListener {

    lateinit var delegatesAdapter: DelegatesAdapter
    var delegatesList = ArrayList<CallCenterDelegateData>()
    var page = 1
    var hasMorePages = false
    var webService : WebService?=null
    var progressDialog : Dialog?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!

        return inflater.inflate(R.layout.fragment_delegates, container, false)
    }

   var currentSearchText = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
       // Log.d("back" , "Delegate crested")
        getDeleagtesData()
        searchIcon?.setOnClickListener{
            renitializePages()
            currentSearchText= searchEditText?.text.toString()
            getDelegatesList()
        }

    }

    private fun renitializePages() {
        scrollingNestedScrollMostSelling?.resetPageCounter() // to 1
        page = 1
        delegatesList.clear()

    }

    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    private fun getDelegatesList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            val hashMap = HashMap<String,Any>()
            hashMap.put("page",page)
            if (currentSearchText.isNotEmpty())
                hashMap.put("name",currentSearchText)
           DelegatePresenter.getDelegatesList(webService!!, delegateObserver(),hashMap)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }

    /*    modelHandleChangeFragmentclass.notifyItemSelected?.observe(activity!!, Observer { datamodel ->

            if (datamodel != null) {

                if (datamodel ==1) {

                    val bundle = Bundle()
                    bundle.putString(NameUtils.CURRENT_DELEGATE, Gson().toJson(delegatesList.get(selectedItemPosition)))
                    UtilKotlin.changeFragmentBack(activity!! ,
                        OrdersFragment(),"delegate"  , bundle,
                        arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)


                }

                modelHandleChangeFragmentclass.setNotifyItemSelected(null) // start details with this data please
            }

        })*/

    }

    var selectedItemPosition = 0
    override fun onDestroyView() {
        disposableObserver?.dispose()
       // modelHandleChangeFragmentclass.notifyItemSelected.removeObservers(activity!!)
        super.onDestroyView()
    }

    var disposableObserver : DisposableObserver<Response<DelegateResponse>>?=null
    private fun delegateObserver(): DisposableObserver<Response<DelegateResponse>> {

        disposableObserver= object : DisposableObserver<Response<DelegateResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<DelegateResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                   // delegatesList.clear()
                    hasMorePages = response.body()?.data?.has_more_page?:false // default false
                   // delegatesList.addAll(response.body()?.data?.list?:ArrayList())
                    delegatesCount?.text = ((delegatesList.size?:0)+(response.body()?.data?.list?.size?:0)).toString()
                    delegatesAdapter.updateData(response.body()?.data?.list?:ArrayList())
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
        renitializePages()
        getDelegatesList()
    }

    private fun getDeleagtesData() {

    //    delegatesList.clear()
    /*    for (i in 0..4)
        {
            delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
        }*/

        delegatesAdapter = DelegatesAdapter(context!! , delegatesList , this,View.GONE)
        delegatesRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = delegatesAdapter
        }


    }

    override fun onItemClick(position: Int) {

      //  Log.d("clickDelegate" , "${delegatesList[position].Id}")

//        UtilKotlin.replaceFragmentWithBack(context!!, this, DelegateDetailsFragment(),
//            null, R.id.frameLayout_direction, 120, false, true)

        val bundle = Bundle()
        // send delegate id to order list 
      bundle.putInt(NameUtils.delegateId,delegatesList.get(position).id?:0)
        bundle.putString(NameUtils.orderName,delegatesList.get(position).name?:"")

        // go to fragment of orders
        UtilKotlin.changeFragmentBack(activity!! ,OrdersFragment() , ""  , bundle,arguments?.getInt(NameUtils.WHICHID,R.id.frameLayout_direction)?:R.id.frameLayout_direction)
    }

    override fun onLoadMore(currentPage: Int) {
        page = currentPage
        if (hasMorePages) {
            getDelegatesList()

        }
    }

    /*   override fun delegateClickListener(position: Int) {
           selectedItemPosition = position
           val bottomSheetActions = BottomSheetActions()
           bottomSheetActions.show(activity?.supportFragmentManager!!, "bottomSheetActions")

       }*/

}