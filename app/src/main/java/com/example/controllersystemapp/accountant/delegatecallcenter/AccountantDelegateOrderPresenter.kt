package com.example.controllersystemapp.accountant.delegatecallcenter

import android.app.Activity
import android.util.Log
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItems
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AccDelegateOrderItemsDetails
import com.example.controllersystemapp.accountant.delegatecallcenter.model.AccountantDelegateDetails
import com.example.controllersystemapp.accountant.delegatecallcenter.model.CallCenterResponse
import com.example.controllersystemapp.accountant.makeorder.models.AccountantMakeOrderRequest
import com.example.controllersystemapp.admin.categories.models.CategoriesListResponse
import com.example.controllersystemapp.admin.storesproducts.models.*
import com.example.controllersystemapp.delegates.makeorder.model.DelegateMakeOrderRequest
import com.example.controllersystemapp.delegates.makeorder.model.DelegateProductsListResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemDetailsResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrderItemsListResponse
import com.example.controllersystemapp.delegates.wallet.models.DelegateOrdersListResponse
import com.example.util.ApiConfiguration.SuccessModel
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AccountantDelegateOrderPresenter {


    fun accountantDelegateDetails(webService: WebService, delegateId : Int?, activity: Activity,
                                  model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")
        webService.accountantDelegateDetails(delegateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccountantDelegateDetails>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccountantDelegateDetails>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }



    fun accountantDelegateOrderItems(webService: WebService, orderId : Int?, activity: Activity,
                                  model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")
        webService.accDelegateOrderItems(orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccDelegateOrderItems>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccDelegateOrderItems>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }



    fun accDelegateOrderItemDetails(webService: WebService, itemId : Int?, activity: Activity,
                                     model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")
        webService.accDelegateOrderItemsDetails(itemId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccDelegateOrderItemsDetails>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccDelegateOrderItemsDetails>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }


    fun accDeleteDelegate(webService: WebService, delegateId : Int, activity: Activity,
                                    model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")
        webService.accountantDeleteDelegate(delegateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<SuccessModel>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<SuccessModel>) {

                    if (response.isSuccessful)
                    {
                        Log.d("testApi" , "responseSuccess")

                        //hideLoader()
                        // model.setShowLoader(false)
                        model.responseCodeDataSetter(response?.body())

                    }
                    else{
                        Log.d("testApi" , "responseError")
                        //model.setShowLoader(false)
                        model.onError(response.errorBody())
                    }



                }

                override fun onError(e: Throwable) {
                    //hideLoader()
                    // model.setShowLoader(false)
                    dispose()
                    Log.d("testApi" , "responsefaile")
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }

}