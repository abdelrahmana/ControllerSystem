package com.smartangle.controllersystemapp.admin.makeorders

import com.smartangle.controllersystemapp.admin.makeorders.model.OrderCreateRequestAdmin
import com.smartangle.controllersystemapp.callcenter.maketalbya.model.OrderCreateRequest
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.SuccessModelV2
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object PresenterMakeOrder {

    fun setTalabyaPost(
        webService: WebService,
        itemObserver: DisposableObserver<Response<SuccessModelV2>>,
        orderCreateRequest: OrderCreateRequestAdmin
    )
    {
        webService.postOrderAdmin(orderCreateRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }
}