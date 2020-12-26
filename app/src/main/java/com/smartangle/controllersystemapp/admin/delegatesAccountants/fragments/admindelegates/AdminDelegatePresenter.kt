package com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates

import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.controllersystemapp.admin.delegatesAccountants.fragments.admindelegates.model.DelegateListResponse
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AdminDelegatePresenter {
    fun getDelegatesInAdmin(webService: WebService, delegateObserver: DisposableObserver<Response<DelegateListResponse>>,pageNumber : Int)
    {

        webService.getDelegatesAdmin(pageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(delegateObserver)



    }
}