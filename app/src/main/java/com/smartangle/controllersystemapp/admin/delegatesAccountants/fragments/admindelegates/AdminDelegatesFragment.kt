package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.CallCenterPresnter
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.AdminDelegatesAdapter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.adapters.DelegatesAdapter
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DataBean
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DelegateListResponse
import com.smartangle.controllersystemapp.admin.interfaces.OnRecyclerItemClickListener
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.EndlessRecyclerOnScrollListener
import com.smartangle.util.NameUtils
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_acc_delegates.*
import kotlinx.android.synthetic.main.fragment_admin_delegates.*
import retrofit2.Response


class AdminDelegatesFragment : Fragment(), OnRecyclerItemClickListener {

    var webService: WebService? = null
    var progressDialog: Dialog? = null
    lateinit var modelHandleChangeFragmentclass: ViewModelHandleChangeFragmentclass

    lateinit var adminDelegatesAdapter: AdminDelegatesAdapter
    var delegatesList = ArrayList<DataBean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webService = ApiManagerDefault(context!!).apiService // auth
        progressDialog = UtilKotlin.ProgressDialog(activity!!)
        modelHandleChangeFragmentclass = UtilKotlin.declarViewModel(activity!!)!!

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_delegates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPaginationListener()
       setInitalAdapter()

    }

    private fun setInitalAdapter() {
        adminDelegatesAdapter = AdminDelegatesAdapter(context!!, delegatesList, this)
        adminDelegateRecycler?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!! , RecyclerView.VERTICAL , false)
            adapter = adminDelegatesAdapter
            addOnScrollListener(paginationListener)
        }
    }

    var hasMorePages = false
    var pageNumber = 1
    lateinit var paginationListener : EndlessRecyclerOnScrollListener
    fun setPaginationListener() {
        paginationListener = object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore(currentPage: Int, visibleItemPosition: Int) {
                if (hasMorePages) {
                    pageNumber = currentPage
                    getDelegateList()
                }

            }

            override fun positionChange(lastVisibleItem: Int) {
            }
        }
    }
    override fun onResume() {
        super.onResume()
        delegatesList.clear()

        getDelegatesList()
    }
    private fun getDelegatesList() {

        if (UtilKotlin.isNetworkAvailable(context!!)) {
            progressDialog?.show()
            AdminDelegatePresenter.getDelegatesInAdmin(webService!!, getDelegateList(),pageNumber)
        } else {
            progressDialog?.dismiss()
            UtilKotlin.showSnackErrorInto(activity, getString(R.string.no_connect))

        }
    }
    var disposableObserver : DisposableObserver<Response<DelegateListResponse>>?=null
    private fun getDelegateList(): DisposableObserver<Response<DelegateListResponse>> {

        disposableObserver= object : DisposableObserver<Response<DelegateListResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<DelegateListResponse>) {
                if (response!!.isSuccessful) {
                    progressDialog?.dismiss()
                    //delegatesList.clear()
                    //delegatesList.addAll(response.body()?.data?.list?:ArrayList())
                    adminDelegatesAdapter.updateData(response.body()?.data?.list?:ArrayList())
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
    private fun getDeleagtesData() {

        //    delegatesList.clear()
        /*    for (i in 0..4)
            {
                delegatesList.add(DelegatesModel("احمد حازم" , null , " +966 56784 9876" , i+1))
            }*/
        //delegatesList.clear()

        adminDelegateCount?.text = delegatesList.size.toString()



    }

    override fun onDestroyView() {
        disposableObserver?.dispose()
        super.onDestroyView()
    }

    override fun onItemClick(position: Int) {
        val bundle = Bundle()
        bundle.putInt(NameUtils.WHICHID, R.id.frameLayout_direction)
        bundle.putInt(ADMIN_DELEGATE_ID, delegatesList[position].id?:0)

        UtilKotlin.changeFragmentBack(activity!!,
            DelegateDetailsFragment(), "", bundle, R.id.frameLayout_direction)
    }

    companion object{

        val ADMIN_DELEGATE_ID = "adminDelegateId"

    }
}