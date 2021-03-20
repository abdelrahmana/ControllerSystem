package com.smartangle.controllersystemapp.admin.reports.purchasereports

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.photonect.photographerapp.notificationphotographer.DonePackgae.PurchaseAdapterFragment
import com.smartangle.controllersystemapp.R
import com.smartangle.controllersystemapp.admin.reports.ReportsFilterBottomSheet
import com.smartangle.controllersystemapp.admin.reports.ReportsPresenter.getPurchaseReport
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import com.smartangle.controllersystemapp.admin.reports.ReportsPresenter.getReportsSales
import com.smartangle.controllersystemapp.admin.reports.model.Data
import com.smartangle.controllersystemapp.admin.reports.model.Datax
import com.smartangle.controllersystemapp.admin.reports.model.PurchaseResponse
import com.smartangle.controllersystemapp.admin.reports.model.SalesReportResponse
import com.smartangle.util.ApiConfiguration.ApiManagerDefault
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_sales.*
import retrofit2.Response


class PurchaseReportFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var model :ViewModelHandleChangeFragmentclass
    lateinit var webService : WebService
    lateinit var progressDialog : Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        progressDialog = UtilKotlin.ProgressDialog(context!!)
        model = UtilKotlin.declarViewModel(activity!!)!!
        webService = ApiManagerDefault(context!!).apiService
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header?.text = getString(R.string.purchase)
        filterButton?.setOnClickListener {
            showBottomSheet(null)
        }

        backButtonSales?.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        getPurchaseReports()
        setViewModelListener()
    }
    var purchaseAdapter : PurchaseAdapterFragment?=null
    val arrayList = ArrayList<Datax>()
    private fun setRecycleViewData(data: ArrayList<Datax>?) {
        arrayList.clear()
        arrayList?.addAll(data?:ArrayList())
       // arrayList.add("")
        purchaseAdapter = PurchaseAdapterFragment(model,arrayList)
        UtilKotlin.setRecycleView(salesRecycleView,purchaseAdapter!!,
            RecyclerView.VERTICAL,context!!, null, true)
    }


    override fun onDestroyView() {
        model.notifyItemSelected.removeObservers(activity!!)
        disposableObserver?.dispose()
        super.onDestroyView()
    }

    fun setViewModelListener() {
        model?.notifyItemSelected?.observe(activity!!, Observer<Any> { modelSelected ->
            if (modelSelected != null) { // if null here so it's new service with no any data
                if (modelSelected is Data) { // get id of report to start the details or get full object
                    // if (modelSelected.isItCurrent) {
                    // initSlider(modelSelected.pictures)
                    // }
                    val bundle = Bundle()
                    //     bundle.putInt(EXITENCEIDPACKAGE,availableServiceList.get(position).id?:-1)
                  //  UtilKotlin.changeFragmentWithBack(activity!! , R.id.container , ReportsDetailsFragment() , bundle)

           //         UtilKotlin.changeFragmentBack(activity!! , ReportsDetailsFragment() , "ReportsDetailsFragment"  ,
             //           null,R.id.frameLayout_direction)

                }

                model?.setNotifyItemSelected(null) // remove listener please from here too and set it to null
            }
        })
    }

    fun getPurchaseReports() {
        if(UtilKotlin.isNetworkAvailable(context!!))
        {
            progressDialog?.show()
            getPurchaseReport(webService!!,dispossibleObserver() ,HashMap<String,Any>())

        }
        else{
            UtilKotlin.showSnackMessage(activity!!,getString(R.string.no_connect))
        }
    }

    var disposableObserver : DisposableObserver<Response<PurchaseResponse>>?=null
    private fun dispossibleObserver(): DisposableObserver<Response<PurchaseResponse>> {

        disposableObserver= object : DisposableObserver<Response<PurchaseResponse>>() {
            override fun onComplete() {
                progressDialog?.dismiss()
                dispose()
            }

            override fun onError(e: Throwable) {
                UtilKotlin.showSnackErrorInto(activity!!, e.message.toString())
                progressDialog?.dismiss()
                dispose()
            }

            override fun onNext(response: Response<PurchaseResponse>) {
                progressDialog?.dismiss()
                if (response!!.isSuccessful) {

                    setRecycleViewData(response?.body()?.data)// when data is coming

                }
                else
                {
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
    private fun showBottomSheet(bundle: Bundle?) { // show bottom sheet
        val reportsFilterBottomSheet = ReportsFilterBottomSheet()
        reportsFilterBottomSheet.arguments = bundle
        reportsFilterBottomSheet.show(
            activity!!.supportFragmentManager,
            "reports_sheet"
        )
    }
    companion object {
    }
}