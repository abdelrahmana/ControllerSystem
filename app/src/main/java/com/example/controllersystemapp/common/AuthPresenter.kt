package com.example.controllersystemapp.common

import com.example.controllersystemapp.common.login.LoginRequest
import com.example.controllersystemapp.common.login.LoginResponse
import com.example.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AuthPresenter {

    fun getLoginResponse(
        mServiceUser : WebService,
        loginRequest: LoginRequest,
        responseDisposableObserver: DisposableObserver<Response<LoginResponse>>
    ) {
        mServiceUser.login(loginRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(responseDisposableObserver)
    }


}