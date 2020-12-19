package com.smartangle.controllersystemapp.accountant.delegatecallcenter

import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.smartangle.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object CallCenterPresnter {

    fun getCallCenter(webService: WebService,callCenterObserver: DisposableObserver<Response<CallCenterResponse>>)
    {

        webService.getCallCenterList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)



    }
    fun getDelegates(webService: WebService,callCenterObserver: DisposableObserver<Response<CallCenterResponse>>)
    {

        webService.getDelegates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)



    }

    fun addDelegateApi(webService: WebService,callCenterObserver: DisposableObserver<Response<SuccessModel>>,callCenterRequest: AddDelegateCallCenterRequest)
    {
        webService.addDelegate(callCenterRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)
    }

    fun addCallCenterApi(webService: WebService,callCenterObserver: DisposableObserver<Response<SuccessModel>>,callCenterRequest: AddDelegateCallCenterRequest)
    {
        webService.addCallCenter(callCenterRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)
    }



    fun editCallCenter(webService: WebService,callCenterObserver: DisposableObserver<Response<SuccessModel>>,callCenterRequest: AddDelegateCallCenterRequest)
    {
        webService.editCallCenter(callCenterRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)
    }

    fun editDelegate(webService: WebService,callCenterObserver: DisposableObserver<Response<SuccessModel>>,callCenterRequest: AddDelegateCallCenterRequest)
    {
        webService.editDelegate(callCenterRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)
    }
}