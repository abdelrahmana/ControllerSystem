package com.example.controllersystemapp.accountant.sales

import android.app.Activity
import com.example.controllersystemapp.accountant.sales.model.ItemListResponses
import com.example.controllersystemapp.accountant.sales.model.SalesResponse
import com.example.controllersystemapp.callcenter.delegate.model.ItemDetailsResponse
import com.example.controllersystemapp.callcenter.delegate.model.ItemListResponse
import com.example.util.ApiConfiguration.WebService
import com.example.util.CommonActivity
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import retrofit2.Response

object SalesPresenter {

    suspend fun getSalesList(model: ViewModelHandleChangeFragmentclass, service: WebService,status : Int,activity:Activity) {
        var callResponse : Response<SalesResponse>? = null
        GlobalScope.async(Dispatchers.IO) {
            // make network call
            // return user
            // var userObject : OffersModel?=  offerApi(service.getOfferList(pageNumber,localeLang).await(),model)
            try {
                /*  val callResponse : Response<DoctorModelResponse>*/ callResponse = service.getSalesList(status)
                if (callResponse != null) {
                    if (callResponse!!.isSuccessful)
                        model.responseCodeDataSetter(callResponse!!.body()!!)
                    else {  //
                        model.onError(callResponse!!.errorBody())
                    }
                }
            }catch (e:Exception){
                UtilKotlin.showSnackErrorInto(activity,e.message.toString())
           //     model.onError(e.message.toString()) // this is onfaliure method
            }
            return@async callResponse?.body()
        }.await()

    }

    fun getItemList(
        webService: WebService,
        itemObserver: DisposableObserver<Response<ItemListResponses>>,
        hashMap: HashMap<String, Any>
    )
    {
        webService.getItemListAccountant(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }

    fun getItemDetailsAccountant(
        webService: WebService,
        itemObserver: DisposableObserver<Response<ItemDetailsResponse>>,
        hashMap: HashMap<String, Any>
    )
    {
        webService.getAccountantItemDetails(hashMap)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemObserver)



    }
}