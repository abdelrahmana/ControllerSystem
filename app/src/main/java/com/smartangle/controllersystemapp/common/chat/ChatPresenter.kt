package com.smartangle.controllersystemapp.common.chat

import com.smartangle.controllersystemapp.common.chat.model.RequestMessgae
import com.smartangle.controllersystemapp.common.chat.model.ResponseChatList
import com.smartangle.controllersystemapp.common.login.LoginRequest
import com.smartangle.controllersystemapp.common.login.LoginResponse
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object ChatPresenter {

    fun sendNewMessagePost(
        mServiceUser : WebService,
        requestMessage: RequestMessgae,
        responseDisposableObserver: DisposableObserver<Response<SuccessModel>>
    ) {
        mServiceUser.requestPostMessage(requestMessage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(responseDisposableObserver)
    }

    fun listMessages(
        mServiceUser : WebService,
        recieverId: Int,
        responseDisposableObserver: DisposableObserver<Response<ResponseChatList>>
    ) {
        mServiceUser.getMessagesList(recieverId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(responseDisposableObserver)
    }
}