package com.example.controllersystemapp.admin.storesproducts

import android.app.Activity
import android.util.Log
import com.example.controllersystemapp.admin.storesproducts.models.ProductsListResponse
import com.example.util.ApiConfiguration.WebService
import com.example.util.UtilKotlin
import com.example.util.ViewModelHandleChangeFragmentclass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

object ProductsPresenter {


    fun getProductsList(webService: WebService, activity: Activity, model: ViewModelHandleChangeFragmentclass)
    {

        Log.d("testApi" , "getData")
        webService.productsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Response<ProductsListResponse>>() {
                override fun onComplete() {

                    //hideLoader()
                  //  model.setShowLoader(false)
                    dispose()

                }

                override fun onNext(response: Response<ProductsListResponse>) {

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
                                model.onError(response.errorBody().toString())
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