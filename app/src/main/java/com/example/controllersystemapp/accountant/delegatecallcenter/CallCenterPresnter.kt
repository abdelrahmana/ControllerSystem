package com.example.controllersystemapp.accountant.delegatecallcenter

import android.app.Activity
import android.util.Log
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.admin.delegatesAccountants.models.AccountantListResponse
import com.example.controllersystemapp.common.login.LoginResponse
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
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