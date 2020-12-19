package com.smartangle.controllersystemapp.callcenter.delegate

import com.smartangle.controllersystemapp.callcenter.delegate.model.DelegateResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.ItemListResponse
import com.smartangle.controllersystemapp.callcenter.delegate.model.OrderResponse
import com.smartangle.util.ApiConfiguration.WebService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object DelegatePresenter {

    fun getDelegatesList(
        webService: WebService,
        callCenterObserver: DisposableObserver<Response<DelegateResponse>>,
        hashMap: HashMap<String, Any>
    )
    {

        webService.getDelegateListInCallCenter(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)



    }

    fun getOrderList(
        webService: WebService,
        callCenterObserver: DisposableObserver<Response<OrderResponse>>,
        hashMap: HashMap<String, Any>
    )
    {

        webService.getOrderList(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callCenterObserver)



    }

    fun getItemList(
        webService: WebService,
        itemObserver: DisposableObserver<Response<ItemListResponse>>,
        hashMap: HashMap<String, Any>
    )
    {
        webService.getItemList(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }

    fun getItemDetails(
        webService: WebService,
        itemObserver: DisposableObserver<Response<ItemDetailsResponse>>,
        hashMap: HashMap<String, Any>
    )
    {
        webService.getItemDetailsData(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }
}