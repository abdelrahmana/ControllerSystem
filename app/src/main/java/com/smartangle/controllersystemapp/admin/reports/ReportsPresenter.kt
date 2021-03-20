package com.smartangle.controllersystemapp.admin.reports

import com.smartangle.controllersystemapp.admin.reports.model.PurchaseResponse
import com.smartangle.controllersystemapp.admin.reports.model.SalesReportResponse
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object ReportsPresenter {
    fun getReportsSales(
        mServiceUser: WebService,
        observer: DisposableObserver<Response<SalesReportResponse>>,hashMap: HashMap<String,Any>
    ) {

        mServiceUser.getSalesResponse(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
    fun getPurchaseReport(
        mServiceUser: WebService,
        observer: DisposableObserver<Response<PurchaseResponse>>,hashMap: HashMap<String,Any>
    ) {

        mServiceUser.getPurchaseResponse(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

}