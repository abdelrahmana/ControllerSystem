package com.smartangle.controllersystemapp.admin.settings.editprofile

import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.util.ApiConfiguration.WebService
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