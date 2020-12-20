package com.smartangle.controllersystemapp.common

import android.app.Activity
import com.smartangle.controllersystemapp.common.forgetpassword.model.RequestModelNewPass
import com.smartangle.controllersystemapp.common.login.LoginRequest
import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
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