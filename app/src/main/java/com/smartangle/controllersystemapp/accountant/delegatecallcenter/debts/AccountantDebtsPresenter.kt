package com.smartangle.controllersystemapp.accountant.delegatecallcenter.debts

import android.app.Activity
import android.util.Log
import com.smartangle.util.ApiConfiguration.SuccessModel
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AccountantDebtsPresenter {

    fun accountantDebtsList(webService: WebService, page : Int , delegateId : Int,
                              activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")

        webService.accountantDebtsList(page , delegateId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccountantDebtsListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccountantDebtsListResponse>) {

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
                    //model.onStringError(e.message.toString())
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }



    fun accountantEditDebt(webService: WebService, id : Int , quantity : Int ,
                        activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        val params: LinkedHashMap<String, Any> = LinkedHashMap()
        params["id"] = id
        params["quantity"] = quantity

        Log.d("testApi" , "getData")
        webService.accountantUpdateDebt(params)
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



    fun deleteDebts(webService: WebService, debstId : Int , activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")

        webService.accountantDeleteDebts(debstId)
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
                    //model.onStringError(e.message.toString())
                    UtilKotlin.showSnackErrorInto(activity!! , e.message.toString())
                }


            })



    }



    fun accountantCreateDebts(webService: WebService, delegateId : Int , productId : Int ,
                              quantity : Int , activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        val params: LinkedHashMap<String, Any> = LinkedHashMap()
        params["user_id"] = delegateId
        params["product_id"] = productId
        params["quantity"] = quantity

        Log.d("testApi" , "getData")
        webService.accountantCreateDebts(params)
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