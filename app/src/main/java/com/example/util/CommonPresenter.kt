package com.example.util

import com.example.controllersystemapp.accountant.delegatecallcenter.model.AddDelegateCallCenterRequest
import com.example.controllersystemapp.admin.settings.editpassword.EditPasswordRequest
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object CommonPresenter {

    fun editPassword(webService: WebService, callCenterObserver: DisposableObserver<Response<SuccessModel>>, changePass: EditPasswordRequest)
    {

        webService.changePassword(changePass)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)



    }
}