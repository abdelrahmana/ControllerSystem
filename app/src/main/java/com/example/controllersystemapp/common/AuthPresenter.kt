package com.example.controllersystemapp.common

import android.app.Activity
import android.content.Context
import com.example.controllersystemapp.common.forgetpassword.model.RequestModelNewPass
import com.example.controllersystemapp.common.login.LoginRequest
import com.example.controllersystemapp.common.login.LoginResponse
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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

    suspend fun setNewPassWord(model: ViewModelHandleChangeFragmentclass, service: WebService,RequestModelNewPass: RequestModelNewPass,context :Activity) {
        var callResponse : Response<SuccessModel>? = null
        GlobalScope.async(Dispatchers.IO) {
            // make network call
            // return user
            // var userObject : OffersModel?=  offerApi(service.getOfferList(pageNumber,localeLang).await(),model)
            try {
                /*  val callResponse : Response<DoctorModelResponse>*/ callResponse = service.setNewPassword(RequestModelNewPass)
                if (callResponse != null) {
                    if (callResponse!!.isSuccessful)
                        model.responseCodeDataSetter(callResponse!!.body()!!)
                    else {  //
                        model.onError(callResponse?.errorBody())
                    }
                }
            }catch (e:Exception){
               UtilKotlin.showSnackErrorInto(context,e.message.toString()) // this is onfaliure method
            }
            return@async callResponse?.body()
        }.await()

    }

    fun postLogOut(
        mServiceAuth : WebService,
        responseDisposableObserver: DisposableObserver<Response<SuccessModel>>
    ) {
        mServiceAuth.loginOut()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(responseDisposableObserver)
    }
}