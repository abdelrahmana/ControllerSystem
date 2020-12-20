package com.smartangle.controllersystemapp.accountant.products

import android.app.Activity
import android.util.Log
import com.smartangle.controllersystemapp.accountant.products.models.AccountantProdDetailsResponse
import com.smartangle.controllersystemapp.accountant.products.models.AccountantProductsListResponse
import com.smartangle.util.ApiConfiguration.WebService
import com.smartangle.util.UtilKotlin
import com.smartangle.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object AccountantProductPresenter {



    fun productsList(webService: WebService,categoryId : Int?, name : String?,
                     activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {
        Log.d("testApi" , "getData")
        webService.accountantProductsList(categoryId , name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccountantProductsListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccountantProductsListResponse>) {

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


    fun productsListDetails(webService: WebService, productId : Int , activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {
        Log.d("testApi" , "getData")
        webService.accProductDetails(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<AccountantProdDetailsResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                    //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<AccountantProdDetailsResponse>) {

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