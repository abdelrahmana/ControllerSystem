package com.example.controllersystemapp.admin.settings.editprofile

import com.example.controllersystemapp.callcenter.maketalbya.model.OrderCreateRequest
import com.example.controllersystemapp.common.login.LoginResponse
import com.example.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import retrofit2.Response

object EditPresenter {

    fun editProfile(
        webService: WebService,
        itemObserver: DisposableObserver<Response<LoginResponse>>,
        requestBody: RequestBody
    )
    {
        webService.editProfileWebService(requestBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }
}